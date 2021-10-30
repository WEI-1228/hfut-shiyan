from threading import Thread

from db import DB
from response_protocol import *
from server_Socket import ServerSocket
from socketWrapper import SocketWrapper


class Server(object):
    '''服务器核心类'''

    def __init__(self):
        # 创建服务器套接字
        self.server_socket = ServerSocket()

        # 创建请求的id和方法关联字典
        self.request_handle_function = {}
        self.register(REQUEST_LOGIN, self.request_login_handle)
        self.register(REQUEST_CHAT, self.request_chat_handle)

        # 创建保存当前用户的字典
        self.clients = {}

        # 创建数据库管理对象
        self.db = DB()

    def register(self, request_id, handle_function):
        '''注册消息类型和处理函数到字典里'''
        self.request_handle_function[request_id] = handle_function

    def startup(self):
        '''获取客户端连接，并提供服务'''
        while True:
            # 获取客户端连接
            print('获取客户端连接~~~')
            soc, addr = self.server_socket.accept()
            print('连接成功')

            # 使用套接字生成包装对象
            client_soc = SocketWrapper(soc)
            # 收发消息
            Thread(target=lambda: self.request_handle(client_soc)).start()

    def request_handle(self, client_soc):
        '''处理客户端请求'''
        while True:
            # 接收客户端数据
            recv_data = client_soc.recv_data()
            if not recv_data:
                # 没有接收到数据客户端应该已经关闭
                self.remove_offline_user(client_soc)
                client_soc.close()
                break

            # 解析数据
            parse_data = self.parse_request_text(recv_data)

            # 分析请求类型，并根据请求类型调用相应的处理函数
            handle_function = self.request_handle_function.get(parse_data['request_id'])
            if (handle_function):
                handle_function(client_soc, parse_data)

    def remove_offline_user(self, client_soc):
        '''客户端下线处理'''
        print('有客户端下线了~~~')
        for username, info in self.clients.items():
            print(username)
            if info['sock'] == client_soc:
                print(self.clients[username])
                del self.clients[username]
                break

    def parse_request_text(self, text):
        '''
        解析客户端发送来的数据
        登录信息：0001|username|password
        聊天信息：0002|username|message
        '''
        print('解析客户端数据:' + text)
        request_list = text.split(DELIMITER)
        # 按照类型解析数据
        request_data = {}
        request_data['request_id'] = request_list[0]

        if request_data['request_id'] == REQUEST_LOGIN:
            # 用户请求登录
            request_data['username'] = request_list[1]
            request_data['password'] = request_list[2]

        elif request_data['request_id'] == REQUEST_CHAT:
            # 发送来聊天信息
            request_data['username'] = request_list[1]
            request_data['messages'] = request_list[2]

        return request_data

    def request_login_handle(self, client_soc, request_data):
        '''处理登录功能'''
        print('收到登录请求，准备处理')
        # 获取到账号密码
        username = request_data['username']
        password = request_data['password']

        # 检查是否能够登录
        res, nickname, username = self.check_user_login(username, password)

        # 登录成功则需要保存当前用户
        if res == '1':
            self.clients[username] = {'sock': client_soc, 'nickname': nickname}

        # 拼接返回给客户端的消息
        response_text = ResponseProtocol.response_login_result(res, nickname, username)

        # 把消息发送给客户端
        client_soc.send_data(response_text)

    def request_chat_handle(self, client_soc, request_data):
        '''处理聊天功能'''
        print('收到消息，准备处理')
        print(request_data)
        # 获取消息内容
        username = request_data['username']
        messages = request_data['messages']
        nickname = self.clients[username]['nickname']

        # 拼接发送给客户端的消息文本
        msg = ResponseProtocol.resopnse_chat(nickname, messages)

        # 转发给在线用户
        for u_name, info in self.clients.items():
            if u_name != username: #不需要向发送消息的账号转发数据
                info['sock'].send_data(msg)

    def check_user_login(self, username, password):
        '''检查用户是否登录成功，并返回检查结果(0/失败,1/成功)，昵称，账号'''
        # 从数据库查询用户信息
        sql = "select * from users where user_name='%s'" % username
        res = self.db.get_one(sql)
        print(res)

        # 没有查询结果，用户不存在，登陆失败
        if not res:
            return '0', '', username
        # 密码不匹配，说明密码错误，登陆失败
        if password != res['user_password']:
            return '0', '', username
        # 否则登陆成功
        return '1', res['user_nickname'], username


if __name__ == '__main__':
    Server().startup()
