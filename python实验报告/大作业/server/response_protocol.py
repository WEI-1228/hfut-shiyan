from config import *


class ResponseProtocol(object):
    '''服务器响应协议的格式字符串处理'''

    @staticmethod
    def response_login_result(result, nickname, username):
        '''
        生成用户登录的结果字符串
        :param result: 值为 0 表示登录失败，值为 1 表示登录成功
        :param nickname: 登录用户的昵称。如果登陆失败则为空
        :param username: 登录用户的账号。登录失败则为空
        :return: 供返回给用户的登录结果协议字符串
        '''
        return DELIMITER.join([RESPONSE_LOGIN_RESULT, result, nickname, username])

    @staticmethod
    def resopnse_chat(nickname,message):
        '''
        生成返回给用户的消息字符串
        :param nickname: 发送消息的用户昵称
        :param username: 消息正文
        :return: 返回给用户的消息字符串
        '''
        return DELIMITER.join([RESPONSE_CHAT,nickname,message])
