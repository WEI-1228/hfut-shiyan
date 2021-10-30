import numpy as np
import time
import matplotlib.pyplot as plt


class SVD:
    def __init__(self, k, num_epochs, lr=0.01):
        self.k = k# 矩阵分解出的特征维度
        self.num_epochs = num_epochs # 迭代次数
        self.lr = lr  # 学习率
        self.lamb = 0.015  # 正则项

    def train(self, info_dir, train_dir):
        # 训练数据集
        train_set = list()
        with open(info_dir) as fin:
            num_user = int(fin.readline())  # 读取用户数量
            num_item = int(fin.readline())  # 读取物品数量
        self.item_by_user = [[] for _ in range(num_user)]  # 物品用户倒排表
        with open(train_dir) as fin:
            for line in fin:
                spt = line.split('\t')  # 将每行的数据切割开
                train_set.append([int(spt[0]) - 1, int(spt[1]) - 1, int(spt[2])])  # 向训练集添加一条训练数据
                self.item_by_user[int(spt[0]) - 1].append(int(spt[1]) - 1)  # 向倒排表中添加一条数据

        train_set = np.array(train_set)

        num_rating = train_set.shape[0]  # 获得所有评分的条数，等于训练集的大小
        self.avg_train = train_set[:, 2].sum() / num_rating  # 求出
        self.p_u = np.zeros((num_user, self.k)) + .1
        self.q_i = np.zeros((num_item, self.k)) + .1
        self.b_u = np.zeros(num_user) + .1
        self.b_i = np.zeros(num_item) + .1
        self.y_u = np.zeros((num_item, self.k)) + .1

        time_all = time.time()
        train_errors = []
        for epoch in range(1, self.num_epochs + 1):
            print("epoch : %d" % epoch)
            rmseSum = 0
            # 记录每一轮的迭代时间
            start_time = time.time()
            # 对训练集进行一次遍历训练
            for sample in train_set:
                # 得到用户，物品和评分
                user, item, R = sample[0], sample[1], sample[2]
                # 得到该用户交互过的物品的列表
                nu = self.item_by_user[user]
                n_u_sqrt = np.sqrt(len(nu))
                y = self.y_u[nu].sum(axis=0)
                y_sum_sqrt = y / n_u_sqrt

                # 对于该条记录的预测结果
                r_u = self.avg_train + self.b_u[user] + self.b_i[item] + \
                      np.dot(self.q_i[item], (self.p_u[user] + y_sum_sqrt).T)

                # 误差
                error = R - r_u
                # print(error)
                rmseSum += error ** 2

                # 目标函数对用户参数的梯度
                deltaU = error * self.q_i[item] - self.lamb * self.p_u[user]
                # 目标函数对物品参数的梯度
                deltaI = error * (self.p_u[user] + y_sum_sqrt) - self.lamb * self.q_i[item]

                # 依据梯度下降法对所有的参数进行更新
                self.y_u[nu] += self.lr * (error / n_u_sqrt * self.q_i[nu] - self.lamb * self.y_u[nu])
                self.p_u[user] += self.lr * deltaU
                self.q_i[item] += self.lr * deltaI
                self.b_u[user] += self.lr * (error - self.lamb * self.b_u[user])
                self.b_i[item] += self.lr * (error - self.lamb * self.b_i[item])

            # 计算该轮的rmse损失
            rmse = np.sqrt(rmseSum / num_rating)
            print("RMSE in epoch %d : %f, cost time : %.4f" % (epoch, rmse, time.time() - start_time))
            print("-" * 50)
            # 将该轮的损失保存进列表以便后续的可视化训练过程
            train_errors.append(rmse)
        print("Training finished in %.4f seconds" % (time.time() - time_all))
        self.plot_rmse(train_errors)

    def evaluate(self, test_dir):
        RMSE = 0
        count = 0
        with open(test_dir) as fin:
            for line in fin:
                count += 1
                spt = line.split("\t")
                user, item, R = int(spt[0]) - 1, int(spt[1]) - 1, int(spt[2])
                nu = self.item_by_user[user]
                n_u_sqrt = np.sqrt(len(nu))
                y = self.y_u[nu].sum(axis=0)
                y_sum_sqrt = y / n_u_sqrt
                r_u = self.avg_train + self.b_u[user] + self.b_i[item] + \
                      np.dot(self.q_i[item], (self.p_u[user] + y_sum_sqrt).T)
                if r_u < 1:
                    r_u = 1
                elif r_u > 5:
                    r_u = 5
                RMSE += ((R - r_u) ** 2)

        RMSE = np.sqrt(RMSE / count)
        print("The RMSE in test data is %.4f" % RMSE)

    def plot_rmse(self, errors):
        plt.plot(range(self.num_epochs), errors, marker='o', label='Training Data')
        plt.title('loss')
        plt.xlabel('Epochs')
        plt.ylabel('RMSE')
        plt.legend()
        plt.grid()
        plt.show()


if __name__ == '__main__':
    info_dir = 'data/u.info'
    train_dir = 'data/u1.base'
    test_dir = 'data/u1.test'
    k = 20
    num_epochs = 15
    svd = SVD(k, num_epochs)
    svd.train(info_dir, train_dir)
    svd.evaluate(test_dir)
