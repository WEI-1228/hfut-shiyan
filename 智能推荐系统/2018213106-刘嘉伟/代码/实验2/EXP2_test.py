from EXP2_train import Net, num_user
import numpy as np
import torch

net = Net()
net.load_state_dict(torch.load('model.pt'))

with open('data/test_data') as fopen:
    user = 0
    hint = 0
    for line in fopen:
        test_sample = np.array(list(map(int, line.split(' '))))
        out = []
        for i in range(0, 100, 2):
            u = torch.tensor(user)
            p = torch.tensor(int(test_sample[i]))
            n = torch.tensor(int(test_sample[i + 1]))
            p1, p2 = net(u, p, n)
            out.append(p1.item())
            out.append(p2.item())
        user += 1
        nout = np.array(out)
        sorted_id = np.argsort(-nout)
        index = np.where(sorted_id == 0)
        if index[0] < 10: hint += 1

    print('HR in the test is %.4f' % (hint / num_user))
