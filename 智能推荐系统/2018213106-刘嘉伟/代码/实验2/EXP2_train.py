import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import time
from tqdm import tqdm

num_epochs = 5

with open('data/u.info') as fopen:
    num_user = int(fopen.readline())
    num_item = int(fopen.readline())
    num_rating = int(fopen.readline())


class Net(nn.Module):

    def __init__(self):
        super(Net, self).__init__()
        self.embd1 = nn.Embedding(num_user, 30)  # 将用户id转化成30维的编码
        self.embd2 = nn.Embedding(num_item, 50)  # 将物品id转化为50维的编码
        self.fc1 = nn.Linear(80, 60)  # 感知机
        self.fc2 = nn.Linear(60, 30)
        self.fc3 = nn.Linear(30, 10)
        self.fc4 = nn.Linear(10, 1)

    def forward(self, user, pos, neg):
        user_vec = self.embd1(user)
        pos_vec = self.embd2(pos)
        neg_vec = self.embd2(neg)

        pos_x = torch.hstack((user_vec, pos_vec))  # 拼接成正样本
        neg_x = torch.hstack((user_vec, neg_vec))  # 拼接成负样本

        pos_y = F.relu(self.fc1(pos_x))
        neg_y = F.relu(self.fc1(neg_x))

        pos_y = F.relu(self.fc2(pos_y))
        neg_y = F.relu(self.fc2(neg_y))

        pos_y = F.relu(self.fc3(pos_y))
        neg_y = F.relu(self.fc3(neg_y))

        pos_y = self.fc4(pos_y)
        neg_y = self.fc4(neg_y)

        return pos_y, neg_y  # 返回一个正样本的预测结果和一个负样本的预测结果


if __name__ == '__main__':

    train_set = [[] for i in range(num_user)]
    with open('data/train_data') as fopen:
        for line in fopen:
            spts = line.split(' ')
            train_set[int(spts[0])].append([int(spts[1]), int(spts[2])])

    device = torch.device('cuda:0')
    net = Net().to(device)
    net.load_state_dict(torch.load('model.pt'))
    optimizer = optim.Adam(net.parameters(), lr=0.01)
    alltime = time.time()
    for epoch in range(num_epochs):
        print('Epoch %d/%d :' % (epoch + 1, num_epochs))
        start = time.time()
        bpr_loss = 0.
        for user in tqdm(range(num_user)):
            # -mean(log(sigmoid(pos - neg)))
            loss = torch.tensor(0., requires_grad=True)
            for sample in train_set[user]:
                u = torch.tensor(user).to(device)
                pos_x = torch.tensor(sample[0]).to(device)
                neg_x = torch.tensor(sample[1]).to(device)
                pos_y, neg_y = net(u, pos_x, neg_x)
                loss = loss + torch.log(torch.sigmoid(pos_y - neg_y))

            loss = (-1 * loss) / len(train_set[user])
            bpr_loss += loss.item()
            optimizer.zero_grad()
            loss.backward()
            optimizer.step()
        print('cost %d Secs' % (time.time() - start))
        print('BPR loss in epoch %d : %.4f' % (epoch + 1, bpr_loss / len(train_set)))
        print('---------------------------------------')
    print('Training finished in %d Secs' % (time.time() - alltime))
    torch.save(net.state_dict(), 'model.pt')
