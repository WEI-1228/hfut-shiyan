import numpy as np

with open('data/u.info') as fopen:
    num_user = int(fopen.readline())  # 得到用户数量
    num_item = int(fopen.readline())  # 得到物品数量
    num_rating = int(fopen.readline())  # 总评分的数量

R = np.zeros((num_user, num_item))  # 交互矩阵
with open('data/u.data') as fopen:
    for line in fopen:
        spt = line.split('\t')
        R[int(spt[0]) - 1][int(spt[1]) - 1] = 1  # 填充交互矩阵

test_open = open('data/test_data', 'w')
train_open = open('data/train_data', 'w')

for i in range(R.shape[0]):
    pos = np.argwhere(R[i] != 0).squeeze()  # 得到用户i所有交互过的商品
    neg = np.argwhere(R[i] == 0).squeeze()  # 得到用户i所有未交互过的商品id
    test_sample_pos = np.random.choice(pos)  # 测试集中随机获取一个正样本
    test_sample_neg = np.random.choice(neg, 99)  # 随机获取99个负样本
    test_sample = list(np.hstack((test_sample_pos, test_sample_neg)))  # 将正负样本拼接成一个用户的测试数据
    test_sample = [str(x) for x in test_sample]
    test_open.write(' '.join(test_sample) + '\n')
    for t in pos[pos != test_sample_pos]:
        train_open.write(str(i) + ' ' + str(t) + ' ' + str(np.random.choice(neg)) + '\n')  # 生成一条训练数据
