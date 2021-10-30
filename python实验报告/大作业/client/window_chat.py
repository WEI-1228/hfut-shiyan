from tkinter import Toplevel, UNITS
from tkinter.scrolledtext import ScrolledText
from tkinter import Text
from tkinter import Button
from tkinter import END
from time import localtime,strftime,time


class WindowChat(Toplevel):

    def __init__(self):
        super(WindowChat, self).__init__()
        # 设置窗口大小
        window_width = 795
        window_height = 505

        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()

        position_x = (screen_width - window_width) / 2
        position_y = (screen_height - window_height) / 2

        # 设置窗口大小和位置
        self.geometry('%dx%d+%d+%d' % (window_width, window_height, position_x, position_y))

        # 设置窗口不可修改
        self.resizable(False, False)

        # 添加组件
        self.add_widget()


    def add_widget(self):
        '''添加组件方法'''
        # 聊天区
        chat_text_area = ScrolledText(self)
        chat_text_area['width'] = 110
        chat_text_area['height'] = 30
        chat_text_area.grid(row=0, column=0, columnspan=2)

        chat_text_area.tag_config('green', foreground='#008B00')
        chat_text_area.tag_config('system', foreground='red')
        self.children['chat_text_area'] = chat_text_area

        # 输入区
        chat_input_area = Text(self, name='chat_input_area')
        chat_input_area['width'] = 100
        chat_input_area['height'] = 7
        chat_input_area.grid(row=1, column=0, pady=10)

        # 发送按钮
        send_button = Button(self, name='send_button')
        send_button['width'] = 5
        send_button['height'] = 2
        send_button['text'] = '发送'
        send_button.grid(row=1, column=1)

    def set_title(self, title):
        self.title('欢迎 %s 进入聊天室！' % title)

    def on_send_button_click(self, command):
        '''注册事件，当发送按钮被点击时执行command方法'''
        self.children['send_button']['command'] = command

    def get_input(self):
        '''获取输入框内容'''
        return self.children['chat_input_area'].get(0.0, END)

    def clear_input(self):
        '''清空输入框'''
        self.children['chat_input_area'].delete(0.0, END)

    def append_message(self, sender, message):
        '''添加消息到聊天区'''
        send_time = strftime('%Y-%m-%d %H:%M:%S',localtime(time()))
        send_info = '%s: %s \n' % (sender, send_time)
        self.children['chat_text_area'].insert(END, send_info,'green')
        self.children['chat_text_area'].insert(END, ' '+message+'\n')


        # 向下滚动屏幕
        self.children['chat_text_area'].yview_scroll(3,UNITS)

    def on_window_closed(self,command):
        '''注册关闭窗口时执行的指令'''
        self.protocol('WM_DELETE_WINDOW',command)


if __name__ == '__main__':
    WindowChat().mainloop()
