package xyz.liujiawei;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Svd {
    double lambda;
    int k;
    double lr;
    int num_epochs;

    Svd(int k, double lr, int num_epochs, double lambda) {
        this.k = k;
        this.lr = lr;
        this.num_epochs = num_epochs;
        this.lambda = lambda;
    }

    int userNums;
    int itemNums;
    int ratingNums;
    double avg_train;
    List<List<Integer>> trainSet;
    List<List<Integer>> itemByUser;
    double[] b_u;
    double[] b_i;
    double[][] p_u;
    double[][] q_i;
    double[][] y_u;

    public void init(String infoPath, String trainPath) throws IOException {
        Random random = new Random();
        BufferedReader infoReader = new BufferedReader(new FileReader(infoPath));
        userNums = Integer.parseInt(infoReader.readLine());
        itemNums = Integer.parseInt(infoReader.readLine());
        avg_train = 0;
        trainSet = new LinkedList<>();
        itemByUser = new ArrayList<>(userNums);
        b_u = new double[userNums];
        b_i = new double[itemNums];
        p_u = new double[userNums][this.k];
        for (int i = 0; i < p_u.length; i++) {
            for (int j = 0; j < p_u[0].length; j++) p_u[i][j] = (random.nextDouble() - 0.5) * 0.2;
        }
        q_i = new double[itemNums][this.k];
        for (int i = 0; i < q_i.length; i++) {
            for (int j = 0; j < q_i[0].length; j++) q_i[i][j] = (random.nextDouble() - 0.5) * 0.2;
        }
        y_u = new double[itemNums][this.k];
        for (double[] doubles : y_u) {
            Arrays.fill(doubles, 0.1);
        }
        for (int i = 0; i < userNums; i++) itemByUser.add(new ArrayList<>());
        infoReader = new BufferedReader(new FileReader(trainPath));
        String s;
        while ((s = infoReader.readLine()) != null) {
            String[] split = s.split("\t");
            List<Integer> list = new ArrayList<>();
            list.add(Integer.parseInt(split[0]) - 1);
            list.add(Integer.parseInt(split[1]) - 1);
            list.add(Integer.parseInt(split[2]));
            avg_train += list.get(2);
            itemByUser.get(list.get(0)).add(list.get(1));
            trainSet.add(list);
        }
        ratingNums = trainSet.size();
        avg_train = avg_train / ratingNums;
        infoReader.close();
    }

    public void fit() {
        Date allDate = new Date();
        for (int epoch = 0; epoch < num_epochs; epoch++) {
            System.out.printf("Epochs : %d%n", epoch + 1);
            double rmse = 0;
            Date startDate = new Date();
            for (List<Integer> sample : trainSet) {
                int user = sample.get(0), item = sample.get(1), R = sample.get(2);
                List<Integer> nu = itemByUser.get(user);
                double sqrt_nu = Math.sqrt(nu.size());
                double[] y = new double[this.k];
                for (Integer i : nu) {
                    for (int j = 0; j < y.length; j++) {
                        y[j] += y_u[i][j];
                    }
                }
                for (int i = 0; i < y.length; i++) {
                    y[i] = y[i] / sqrt_nu;
                }
                double[] midArray = new double[this.k];
                for (int i = 0; i < midArray.length; i++) {
                    midArray[i] = y[i] + p_u[user][i];
                }
                double mid = 0;
                for (int i = 0; i < midArray.length; i++) {
                    mid += midArray[i] * q_i[item][i];
                }

                double r_u = this.avg_train + this.b_u[user] + this.b_i[item] + mid;
                double error = R - r_u;
//                System.out.println(error);
                rmse += Math.pow(error, 2);

                double[] deltaU = new double[this.k];
                double[] deltaI = new double[this.k];
                for (int i = 0; i < this.k; i++) {
                    deltaU[i] = error * q_i[item][i] - lambda * p_u[user][i];
                    deltaI[i] = error * (p_u[user][i] + y[i]) - lambda * q_i[item][i];
                }

                for (int i = 0; i < this.k; i++) {
                    p_u[user][i] += this.lr * deltaU[i];
                    q_i[item][i] += this.lr * deltaI[i];
                }

                b_u[user] += this.lr * (error - this.lambda * b_u[user]);
                b_i[item] = this.lr * (error - this.lambda * b_i[item]);

                for (Integer i : nu) {
                    for (int j = 0; j < this.k; j++) {
                        y_u[i][j] += this.lr * (error / sqrt_nu * q_i[i][j] - this.lambda * y_u[i][j]);
                    }
                }
            }
            Date endDate = new Date();
            double time = endDate.getTime() - startDate.getTime();
            rmse = Math.pow(rmse / ratingNums, 0.5);
            System.out.printf("RMSE in epoch %d : %f, cost time : %.4f%n", epoch, rmse, (time / 1000));
            System.out.println("-----------------------------------------------");
        }
        double all = new Date().getTime() - allDate.getTime();
        System.out.printf("Training finished in %.4f seconds%n", all / 1000);
    }

    public void evalute(String testPath) throws IOException {
        BufferedReader testReader = new BufferedReader(new FileReader(testPath));
        String s;
        double RMSE = 0;
        int count = 0;
        while ((s = testReader.readLine()) != null) {
            count++;
            String[] split = s.split("\t");
            int user = Integer.parseInt(split[0]) - 1;
            int item = Integer.parseInt(split[1]) - 1;
            int R = Integer.parseInt(split[2]);
            List<Integer> nu = itemByUser.get(user);
            double sqrt_nu = Math.sqrt(nu.size());
            double[] y = new double[this.k];
            for (Integer i : nu) {
                for (int j = 0; j < y.length; j++) {
                    y[j] += y_u[i][j];
                }
            }
            for (int i = 0; i < y.length; i++) {
                y[i] = y[i] / sqrt_nu;
            }
            double[] midArray = new double[this.k];
            for (int i = 0; i < midArray.length; i++) {
                midArray[i] = y[i] + p_u[user][i];
            }
            double mid = 0;
            for (int i = 0; i < midArray.length; i++) {
                mid += midArray[i] * q_i[item][i];
            }
            double r_u = this.avg_train + this.b_u[user] + this.b_i[item] + mid;
            if (r_u < 1) r_u = 1;
            else if (r_u > 5) r_u = 5;
            RMSE += Math.pow(R - r_u, 2);
        }
        RMSE = Math.pow(RMSE / count, 0.5);
        System.out.printf("The RMSE in test data is %.4f", RMSE);
    }

    public static void main(String[] args) throws IOException {
        Svd svd = new Svd(20, 0.01, 15, 0.02);
        svd.init("L:\\documents\\java-code\\RecommendSystem\\src\\xyz\\liujiawei\\data\\u.info", "L:\\documents\\java-code\\RecommendSystem\\src\\xyz\\liujiawei\\data\\u1.base");
        svd.fit();
        svd.evalute("L:\\documents\\java-code\\RecommendSystem\\src\\xyz\\liujiawei\\data\\u1.test");
    }
}
