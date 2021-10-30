import cv2
import pickle
import os
import numpy as np
from sklearn import preprocessing

with open('./model.pkl', 'rb') as file:
    model = pickle.load(file)

test_path = 'L:\\Python\\Pytorch\\CV\\exp3\\testdata'

img_list = []
label_list = []

for imgs in os.listdir(test_path):
    img_path = os.path.join(test_path, imgs)
    img = cv2.imread(img_path)
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    resize_img = cv2.resize(gray, (28, 28))
    ret, binary_img = cv2.threshold(resize_img, 127, 255, cv2.THRESH_BINARY_INV)
    cv2.imwrite('./binarydata/' + imgs, binary_img)
    img_list.append(binary_img)
    label_list.append(eval(imgs.split('.')[0]))
# cv2.imshow('img',img_list[0])
# cv2.waitKey(0)

f = img_list[0].reshape(1, -1)
for i in range(1, len(img_list)):
    f = np.vstack((f, img_list[i].reshape(1, -1)))

f = preprocessing.StandardScaler().fit_transform(f)
pred = model.predict(f)
pred = list(pred)
for i in range(len(label_list)):
    if label_list[i] != pred[i]:
        cv2.imwrite("testerrorimg/" + str(i) + ".jpg",img_list[i])

for pair in zip(pred,label_list):
    print(pair)
