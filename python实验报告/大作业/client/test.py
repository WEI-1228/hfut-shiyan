import tkinter
from tkinter.scrolledtext import ScrolledText


def test():
    '''客户端测试'''
    window = tkinter.Tk()

    # reset_bt=tkinter.Button(window,name='reset')
    # reset_bt['text'] = '重置'
    # reset_bt.grid(row=0,column=1)
    #
    # login_bt=tkinter.Button(window,name='login')
    # login_bt['text'] = '登陆'
    # login_bt.grid(row=1,column=0)

    scroll = ScrolledText(window)
    scroll['width'] = 100
    scroll['height'] = 30
    scroll.grid(row=0,column=0)

    scroll.tag_config('red',foreground='#ff0000')

    scroll.insert(tkinter.END,'hahaha\n')
    scroll.insert(tkinter.END,'11111','red')

    window.mainloop()


if __name__ == '__main__':
    test()