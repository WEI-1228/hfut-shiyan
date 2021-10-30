import socket
from config import *


class ServerSocket(socket.socket):
    '''自定义套接字，负责初始化服务器套接字需要的相关参数'''

    def __init__(self):
       # 设置为TCP类型
       super(ServerSocket,self).__init__(socket.AF_INET,socket.SOCK_STREAM)

       # 绑定地址和端口号
       self.bind((SERVER_IP,SERVER_PORT))

       # 设置为监听模式
       self.listen(128)
