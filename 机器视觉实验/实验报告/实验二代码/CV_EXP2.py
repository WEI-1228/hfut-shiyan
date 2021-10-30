import cv2
import os
from sklearn.cluster import KMeans
import numpy as np
import pickle
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
import matplotlib.pyplot as plt
import math

base_path = "C:\\Users\\WEI\\Desktop\\ethz_toys\\eccv04dataset"
train_path = os.path.join(base_path, "models")
test_path = os.path.join(base_path, "test")


def detect_SIFT(file_path, n_features):
    """
    检测描述特征
    :param n_features: 检测前n_features个sift特征
    :param file_path:
    :return: 标签，大矩阵，每包含每张图像的描述的列表
    """
    feature_list = []  # 存储每个图像的描述特征
    input_x = []  # 用来聚类的大特征
    labels = []  # 存储图像标签
    for image in os.listdir(file_path):
        filename = os.path.join(file_path, image)
        label = image.split('.')[0]
        # print(filename)
        sift = cv2.xfeatures2d.SIFT_create()
        img = cv2.imread(filename)
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        kpG, desG = sift.detectAndCompute(gray, None)  # 获取关键点和特征描述
        if desG is None:  # 判断特征点是否存在
            continue
        feature_list.append(desG)  # 添加一个描述特征
        labels.append(label)  # 添加对应的标签
        input_x.extend(desG)  # 合并到大矩阵中
    # print(str(len(labels)) + " images detected")
    return labels, input_x, feature_list


def create_train_and_test(n_features):
    # train
    train_label_list, train_x, train_feature_x = detect_SIFT(train_path, n_features)
    train_input = np.array(train_x)
    train = {
        'label': train_label_list,
        'train_x': train_input,
        'feature': train_feature_x
    }
    # print(input_train.shape)
    # print(len(feature_list))
    # print(len(label_list))
    output = open('train.data', 'wb')
    pickle.dump(train, output)

    # test
    test_label_list, test_x, test_feature_x = detect_SIFT(test_path, n_features)
    test_input = np.array(test_x)
    test = {
        'label': test_label_list,
        'test_x': test_input,
        'feature': test_feature_x
    }
    output = open('test.data', 'wb')
    pickle.dump(test, output)
    output.close()


def load_data():
    fin = open("train.data", 'rb')
    train = pickle.load(fin)
    fin = open("test.data", 'rb')
    test = pickle.load(fin)
    fin.close()
    return train, test


# 用一个sift特征描述向量构建一个训练输入向量
def get_features(descriptor, centers, word_size):
    feature_vec = np.zeros([word_size])
    for col in descriptor:
        diffMat = np.tile(col, (word_size, 1)) - centers  # 将该列的特征（128*1）扩大word_size(聚类中心个数)倍然后与每个中心相减
        sqSum = (diffMat ** 2).sum(axis=1)  # 平方相加
        distances = sqSum ** 0.5  # 开根
        index = np.argsort(distances)  # 得到排序后的索引
        feature_vec[index[0]] += 1  # 最相近的那个中心加一
    return feature_vec

# 创建所有图片的训练输入特征向量
def get_all_feature(desc_vec, word_size, clusters):
    vec = np.zeros((len(desc_vec), word_size), 'float32')
    for i in range(len(desc_vec)):
        vec[i] = get_features(desc_vec[i], clusters, word_size)
    return vec


def create_feature(n):
    train, test = load_data()
    train_x = train['train_x']
    train_feature = train['feature']

    test_feature = test['feature']

    kmeans = KMeans(n_clusters=n).fit(train_x)
    print('clusters has created successfully!')
    clusters = kmeans.cluster_centers_
    x = get_all_feature(train_feature, n, clusters)
    x1 = get_all_feature(test_feature, n, clusters)
    output = open("feature.data", 'wb')
    pickle.dump(x, output)
    output = open("test_feature.data", 'wb')
    pickle.dump(x1, output)
    output.close()
    print("feature created successfully!!!")
    # print(x.shape)


# 只检测最相似的一个图像
def predict():
    # 加载(zai)训练特征
    fin = open("feature.data", 'rb')
    x_train = pickle.load(fin)
    # print(x_train.shape)

    # 加载(zai)测试特征
    fin = open("test_feature.data", 'rb')
    x_test = pickle.load(fin)
    fin.close()

    train, test = load_data()
    # 加载(zai)训练标签
    y_train = train['label']

    # 加载(zai)测试数据
    y_test = test['label']

    # 归一化
    sc = StandardScaler()
    x_train = sc.fit_transform(x_train)
    x_test = sc.fit_transform(x_test)

    # 训练模型
    classifier = SVC(C=20, probability=True)
    classifier.fit(x_train, y_train)

    pred = classifier.predict(x_test)

    # 绘图
    for pair in zip(pred, y_test):
        src = os.path.join(test_path, pair[1] + '.tif')
        dst = os.path.join(train_path, pair[0] + '.tif')
        fig = plt.figure()
        m1 = plt.imread(src)
        m2 = plt.imread(dst)
        plt.subplot(1, 2, 1)
        plt.imshow(m1)
        plt.xticks([])
        plt.yticks([])
        plt.subplot(1, 2, 2)
        plt.imshow(m2)
        plt.xticks([])
        plt.yticks([])
        output_dir = os.path.join(base_path, "output")
        output_name = os.path.join(output_dir, pair[0] + '_' + pair[1] + '.jpg')
        plt.savefig(output_name)
        plt.close()

    print('done!')


def save_res(src, dst_list):
    fig = plt.figure()
    lens = math.ceil(len(dst_list) ** 0.5)
    for i in range(len(dst_list)):
        img = plt.imread(dst_list[i])
        plt.subplot(lens, lens, i + 1)
        plt.imshow(img)
        plt.xticks([])
        plt.yticks([])
    output_dir = os.path.join(base_path, "output1")

    output_name = os.path.join(output_dir, src + '_res.jpg')
    plt.savefig(output_name)
    plt.close()


# 检测与最相似概率x%的所有图像
def predict2(x):
    # 加载(zai)训练特征
    fin = open("feature.data", 'rb')
    x_train = pickle.load(fin)
    # print(x_train.shape)

    # 加载(zai)测试特征
    fin = open("test_feature.data", 'rb')
    x_test = pickle.load(fin)
    fin.close()

    train, test = load_data()
    # 加载(zai)训练标签
    y_train = train['label']

    # 加载(zai)测试数据
    y_test = test['label']

    # 归一化
    sc = StandardScaler()
    x_train = sc.fit_transform(x_train)
    x_test = sc.fit_transform(x_test)

    # 训练模型
    classifier = SVC(C=20, probability=True)
    classifier.fit(x_train, y_train)

    pred = classifier.predict(x_test)
    prob = classifier.decision_function(x_test)

    res = np.sort(prob)
    threshold = res[:, -1] * x
    for i in range(threshold.shape[0]):
        index = np.argwhere(prob[i, :] > threshold[i]).tolist()
        index = [x[0] for x in index]
        img_list = []
        for j in index:
            img_name = y_train[j]
            path = os.path.join(train_path, img_name + '.tif')
            img_list.append(path)
        src = os.path.join(test_path, y_test[i] + '.tif')
        img_list.insert(0, src)
        src = y_test[i]
        save_res(src, img_list)
    print('done!')


percentage = 0.8    # 0.8   大于最相似的图像概率*percentage以上概率的图像
n_features = 200  # 200   选取的sift点数量
center_nums = 100  # 100   聚类中心的数量
create_train_and_test(n_features)
create_feature(center_nums)
predict2(percentage)
