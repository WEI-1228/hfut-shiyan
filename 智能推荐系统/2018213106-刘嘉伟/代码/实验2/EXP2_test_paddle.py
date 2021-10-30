from EXP2_train_paddle import Net, num_user
import paddle.fluid as fluid
import paddle.fluid.dygraph as dygraph
import numpy as np

with fluid.dygraph.guard():
    net = Net()
    para_dict, opt_dict = fluid.load_dygraph('model.pdparams')
    net.load_dict(para_dict)

    with open('data/test_data') as fopen:
        user = 0
        hint = 0
        for line in fopen:
            test_sample = np.array(list(map(int, line.split(' '))))
            out = []
            for i in range(0, 100, 2):
                u = dygraph.to_variable(np.int64([user]))
                p = dygraph.to_variable(np.int64([test_sample[i]]))
                n = dygraph.to_variable(np.int64([test_sample[i + 1]]))
                p1, p2 = net(u, p, n)
                out.append(p1.numpy()[0][0])
                out.append(p2.numpy()[0][0])
            user += 1
            nout = np.array(out)
            sorted_id = np.argsort(-nout).squeeze()
            index = np.where(sorted_id == 0)
            if index[0] < 10: hint += 1

        print('HR in the test is %.4f' % (hint / num_user))
