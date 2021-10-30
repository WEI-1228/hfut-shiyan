import os
import numpy as np
import struct
import time
import cv2
import pickle

from sklearn import preprocessing
from sklearn.svm import SVC

path = 'C:\\Users\\WEI\\Desktop\\ethz_toys\\mnist'


def load_mnist_data(path, kind):
    labels_path = os.path.join(path, '%s-labels.idx1-ubyte' % kind)
    images_path = os.path.join(path, '%s-images.idx3-ubyte' % kind)
    with open(labels_path, 'rb') as lbpath:
        magic, n = struct.unpack('>II', lbpath.read(8))
        labels = np.fromfile(lbpath, dtype=np.uint8)
    with open(images_path, 'rb') as imgpath:
        magic, num, rows, cols = struct.unpack('>IIII', imgpath.read(16))
        images = np.fromfile(imgpath, dtype=np.uint8).reshape(len(labels), 784)
    return images, labels


train_images, train_labels = load_mnist_data(path, 'train')
test_images, test_labels = load_mnist_data(path, 't10k')

X = preprocessing.StandardScaler().fit_transform(train_images)
XT = preprocessing.StandardScaler().fit_transform(test_images)

# train_x = X[0:10000]
# train_y = train_labels[0:10000]

XT = XT[0:10000]
test_labels = test_labels[0:10000]

print(time.strftime('%Y-%m-%d %H:%M:%S'))
print("----------------start training-----------------")
clf = SVC(C=100)
# model_svc = svm.SVC(kernel='poly', C=0.1, gamma=0.01)
clf.fit(X, train_labels)

with open('./model.pkl', 'wb') as file:
    pickle.dump(clf, file)
print(time.strftime('%Y-%m-%d %H:%M:%S'))
print("---------------------done----------------------")

with open('./model.pkl', 'rb') as file:
    clf = pickle.load(file)
predict = clf.predict(XT)
precision = np.mean(predict == test_labels)

for i in range(predict.shape[0]):
    if predict[i] != test_labels[i]:
        cv2.imwrite("errorimg/" + str(i) + '_label' + str(test_labels[i]) + '_pred' + str(predict[i]) + '.jpg',
                    test_images[i].reshape(28, 28))

print(precision)
