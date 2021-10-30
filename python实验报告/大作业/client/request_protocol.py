from config import *

class RequestProtocol(object):

    @staticmethod
    def request_login_result(username,password):
        '''0001|user1|111111   类型|账号|密码'''
        return DELIMITER.join([REQUEST_LOGIN,username,password])

    @staticmethod
    def request_chat(username,message):
        '''0002|user1|message  类型|账号|消息'''
        return DELIMITER.join([REQUEST_CHAT,username,message])