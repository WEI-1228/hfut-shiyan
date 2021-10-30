import cv2
import pickle
import os
import numpy as np
from sklearn import preprocessing

with open('./model.pkl', 'rb') as file:
    model = pickle.load(file)

test_path = 'L:\\Python\\Pytorch\\CV\\exp3\\testimg'

img_list = []
label_list = []

for imgs in os.listdir(test_path):
    img_path = os.path.join(test_path, imgs)
    img = cv2.imread(img_path,cv2.IMREAD_UNCHANGED)
    img_list.append(img)
    label_list.append(imgs.split('.')[0])
# cv2.imshow('img',img_list[0])
# cv2.waitKey(0)

test_x = []

f = img_list[0].reshape(1, -1)
for i in range(1, len(img_list)):
    f = np.vstack((f, img_list[i].reshape(1, -1)))

f = preprocessing.StandardScaler().fit_transform(f)
pred = model.predict(f)
for pair in zip(label_list,pred):
    print(pair)
