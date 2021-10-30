from pymysql import connect
from config import *

class DB(object):
    '''数据库操作管理类'''

    def __init__(self):

        # 连接到数据库
        self.conn = connect(
            host=DB_HOST,
            port=DB_PORT,
            database=DB_NAME,
            user=DB_USER,
            password=DB_PASS,
        )

        # 获取游标
        self.cursor = self.conn.cursor()

    def close(self):
        '''释放数据库资源'''
        self.cursor.close()
        self.conn.close()

    def get_one(self,sql):
        '''使用sql语句查询用户信息'''
        # 执行sql语句
        self.cursor.execute(sql)

        # 获取查询结果
        query_result = self.cursor.fetchone()

        # 判断是否有结果
        if not query_result:return None

        # 获取字段名称列表
        fields=[field[0]for field in self.cursor.description]


        # 使用字典和数据合成字典，供返回使用
        return_data={}
        for field,value in zip(fields,query_result):
            return_data[field]=value

        return return_data

if __name__ == '__main__':
    db=DB()
    data=db.get_one("select * from users where user_name='user2'")
    print(data)
    db.close()

