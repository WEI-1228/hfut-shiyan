import paddle
import paddle.fluid as fluid
import paddle.fluid.dygraph as dygraph
import numpy as np
import time
from tqdm import tqdm

num_epochs = 1

with open('data/u.info') as fopen:
    num_user = int(fopen.readline())
    num_item = int(fopen.readline())
    num_rating = int(fopen.readline())


class Net(fluid.dygraph.Layer):
    def __init__(self):
        super(Net, self).__init__()
        self.embd1 = dygraph.Embedding(size=[num_user, 30])
        self.embd2 = dygraph.Embedding(size=[num_item, 50])
        self.fc1 = dygraph.Linear(80, 60)
        self.fc2 = dygraph.Linear(60, 30)
        self.fc3 = dygraph.Linear(30, 10)
        self.fc4 = dygraph.Linear(10, 1)

    def forward(self, user, pos, neg):
        user_vec = self.embd1(user)
        pos_vec = self.embd2(pos)
        neg_vec = self.embd2(neg)

        pos_x = fluid.layers.concat([user_vec, pos_vec], axis=1)
        neg_x = fluid.layers.concat([user_vec, neg_vec], axis=1)

        pos_y = fluid.layers.relu(self.fc1(pos_x))
        neg_y = fluid.layers.relu(self.fc1(neg_x))

        pos_y = fluid.layers.relu(self.fc2(pos_y))
        neg_y = fluid.layers.relu(self.fc2(neg_y))

        pos_y = fluid.layers.relu(self.fc3(pos_y))
        neg_y = fluid.layers.relu(self.fc3(neg_y))

        pos_y = self.fc4(pos_y)
        neg_y = self.fc4(neg_y)

        return pos_y, neg_y


if __name__ == '__main__':
    train_set = [[] for i in range(num_user)]
    with open('data/train_data') as fopen:
        for line in fopen:
            spts = line.split(' ')
            train_set[int(spts[0])].append([int(spts[1]), int(spts[2])])

    with fluid.dygraph.guard():
        net = Net()
        optimizer = fluid.optimizer.Adam(learning_rate=0.01, parameter_list=net.parameters())
        alltime = time.time()
        for epoch in range(num_epochs):
            print('Epoch %d/%d :' % (epoch + 1, num_epochs))
            start = time.time()
            bpr_loss = np.float(0)
            for user in tqdm(range(num_user)):
                # -mean(log(sigmoid(pos - neg)))
                loss = dygraph.to_variable(np.float32([0]))
                for sample in train_set[user]:
                    u = dygraph.to_variable(np.int64([user]))
                    pos_x = dygraph.to_variable(np.int64([sample[0]]))
                    neg_x = dygraph.to_variable(np.int64([sample[1]]))
                    pos_y, neg_y = net(u, pos_x, neg_x)
                    loss = loss + fluid.layers.log(fluid.layers.sigmoid(pos_y - neg_y))

                loss = (-1 * loss) / len(train_set[user])
                bpr_loss += loss.numpy()
                net.clear_gradients()
                loss.backward()
                optimizer.minimize(loss)
            print('cost %d Secs' % (time.time() - start))
            print('BPR loss in epoch %d : %.4f' % (epoch + 1, bpr_loss / len(train_set)))
            print('---------------------------------------')
        print('Training finished in %d Secs' % (time.time() - alltime))
        model_dict = net.state_dict()
        fluid.save_dygraph(model_dict, "model")
