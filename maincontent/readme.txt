使用方法：
1. 将webextract.jar和cfile-1.4.jar添加到你的工程
2. parser.Main类是主界面，它包含三个方法

（1）static public String get(String filename)
使用默认弯曲度阈值抽取主内容

（2）static public String get(String filename, double th)
用户可以自己设置弯曲度阈值0-1

（3）如果上面的方法出现了中文乱码使用
static public String getChinese(String filename)

（4）如果想保持换行符，即保留段落。该方法将<p></p><br>用&hh&符号替换
static public String getWithLineBreak(String filename)