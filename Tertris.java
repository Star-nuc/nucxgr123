package 俄罗斯方块;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Tertris extends JFrame implements KeyListener{
    //游戏的行数26,列数12
    private static final int game_x = 26;
    private static final int game_y = 12;
    //文本域数组
    JTextArea[][] text;
    //二维数组
    int[][] data;
    //显示游戏状态的标签
    JLabel label1;
    //显示游戏分数的标签
    JLabel label;
    //用于判断游戏是否结束
    boolean isrunning;
    //用于存储所有的方块的数组
    int[] allRect;
    //用于存储当前方块的变量
    int rect;
    //线程的休眠时间
    int time = 1000;
    //表示方块坐标
    int x, y;
    //该变量用于计算得分
    int score = 0;
    //定义一个标志变量,用于判断游戏是否暂停
    boolean game_pause = false;
    //定义一个变量用于记录按下暂停键的次数
    int pause_times = 0;

    public void initWindow() {
        //设置窗口大小
        this.setSize(600,850);
        //设置窗口是否可见
        this.setVisible(true);
        //设置窗口居中
        this.setLocationRelativeTo(null);
        //设置释放窗体
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置窗口大小不可变
        this.setResizable(false);
        //设置标题
        this.setTitle("我的俄罗斯方块游戏");
    }

    //初始化游戏界面
    public void initGamePanel() {
        JPanel game_main = new JPanel();
        game_main.setLayout(new GridLayout(game_x,game_y,1,1));
        //初始化面板
        for (int i = 0 ; i < text.length ; i++) {
            for (int j = 0 ; j < text[i].length ;j++) {
                //设置文本域的行列数
                text[i][j] = new JTextArea(game_x,game_y);
                //设置文本域的背景颜色
                text[i][j].setBackground(Color.WHITE);
                //添加键盘监听事件
                text[i][j].addKeyListener(this);
                //初始化游戏边界
                if (j == 0 || j == text[i].length-1 || i == text.length-1) {
                    text[i][j].setBackground(Color.MAGENTA);
                    data[i][j] = 1;
                }
                //设置文本区域不可编辑
                text[i][j].setEditable(false);
                //文本区域添加到主面板上
                game_main.add(text[i][j]);
            }
        }
        //添加到窗口中
        this.setLayout(new BorderLayout());
        this.add(game_main,BorderLayout.CENTER);
    }

    //初始化游戏的说明面板
    public void initExplainPanel() {
        //创建游戏的左说明面板
        JPanel explain_left = new JPanel();
        //创建游戏的右说明面板
        JPanel explain_right = new JPanel();

        explain_left.setLayout(new GridLayout(4,1));
        explain_right.setLayout(new GridLayout(2,1));
        //初始化左说明面板

        //在左说明面板,添加说明文字
        explain_left.add(new JLabel("按空格键,方块变形"));
        explain_left.add(new JLabel("按左箭头,方块左移"));
        explain_left.add(new JLabel("按右箭头,方块右移"));
        explain_left.add(new JLabel("按下箭头,方块下落"));
        //设置标签的内容为红色字体
        label1.setForeground(Color.RED);
        //把游戏状态标签,游戏分数标签,添加到右说明面板
        explain_right.add(label);
        explain_right.add(label1);
        //将左说明面板添加到窗口的左侧
        this.add(explain_left,BorderLayout.WEST);
        //将右说明面板添加到窗口的右侧
        this.add(explain_right,BorderLayout.EAST);
    }

    public Tertris() {
        text = new JTextArea[game_x][game_y];
        data = new int[game_x][game_y];
        //初始化表示游戏状态的标签
        label1 = new JLabel("游戏状态: 正在游戏中!");
        //初始化表示游戏分数的标签
        label = new JLabel("游戏得分为: 0");
        initGamePanel();
        initExplainPanel();
        initWindow();
        //初始化开始游戏的标志
        isrunning = true;
        //初始化存放方块的数组
        allRect = new int[]{0x00cc,0x8888,0x000f,0x888f,0xf888,0xf111,0x111f,0x0eee,0xffff,0x0008,0x0888,0x000e,0x0088,0x000c,0x08c8,0x00e4
        ,0x04c4,0x004e,0x08c4,0x006c,0x04c8,0x00c6};
    }

    public static void main(String[] args) {
        Tertris tertris = new Tertris();
        tertris.game_begin();
    }

    //开始游戏的方法
    public void game_begin() {
        while (true){
            //判断游戏是否结束
            if (!isrunning) {
                break;
            }

            //进行游戏
            game_run();
        }
        //在标签位置显示"游戏结束"
        label1.setText("游戏状态: 游戏结束!");
    }

    //随机生成下落方块形状的方法
    public void ranRect() {
        Random random = new Random();

        rect = allRect[random.nextInt(22)];
    }

    //游戏运行的方法
    public void game_run() {
        ranRect();
        //方块下落位置
        x = 0;
        y = 5;

        for (int i = 0;i < game_x;i++) {
            try {
                Thread.sleep(time);

                if (game_pause) {
                    i--;
                } else {
                    //判断方块是否可以下落
                    if (!canFall(x,y)) {
                        //将data置为1,表示有方块占用
                        changData(x,y);
                        //循环遍历4层,看是否有行可以消除
                        for (int j = x;j < x + 4;j++) {
                            int sum = 0;

                            for (int k = 1;k <= (game_y-2);k++) {
                                if (data[j][k] == 1) {
                                    sum++;
                                }
                            }

                            //判断是否有一行可以被消除
                            if (sum == (game_y-2)) {
                                //消除j这一行
                                removeRow(j);
                            }
                        }
                        //判断游戏是否失败
                        for (int j = 1;j <= (game_y-2);j++) {
                            if (data[3][j] == 1) {
                                isrunning = false;
                                break;
                            }
                        }
                        break;
                    } else {
                        //层数+1
                        x++;
                        //方块下落一行
                        fall(x,y);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //判断方块是否可以继续下落的方法
    public boolean canFall(int m,int n) {
        //定义一个变量
        int temp = 0x8000;
        //遍历4 * 4方格
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((temp & rect) != 0) {
                    //判断该位置的下一行是否有方块
                    if (data[m+1][n] == 1) {
                        return false;
                    }
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
        //可以下落
        return true;
    }

    //改变不可下降的方块对应的区域的值的方法
    public void changData(int m,int n) {
        //定义一个变量
        int temp = 0x8000;
        //遍历整个4 * 4的方块
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((temp & rect) != 0) {
                    data[m][n] = 1;
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    //移除某一行的所有方块,令以上方块掉落的方法
    public void removeRow(int row) {
        int temp = 100;
        for (int i = row;i >= 1;i--) {
            for (int j = 1;j <= (game_y-2);j++) {
                //进行覆盖
                data[i][j] = data[i-1][j];
            }
        }
        //刷新游戏区域
        reflesh(row);

        //方块加速
        if (time > temp) {
            time -= temp;
        }

        score += temp;

        //显示变化后的分数
        label.setText("游戏得分为: " + score);
    }

    //刷新移除某一行后的游戏界面的方法
    public void reflesh(int row) {
        //遍历row行以上的游戏区域
        for (int i = row;i >= 1;i--) {
            for (int j = 1;j <= (game_y-2);j++) {
                if (data[i][j] == 1) {
                    text[i][j].setBackground(Color.BLUE);
                }else {
                    text[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    //方块向下掉落一层的方法
    public void fall(int m,int n) {
        if (m > 0) {
            //清除上一层方块
            clear(m-1,n);
        }
        //重新绘制方块
        draw(m,n);
    }

    //清除方块掉落后,上一层有颜色的地方的方法
    public void clear(int m,int n) {
        //定义变量
        int temp = 0x8000;
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((temp & rect) != 0) {
                    text[m][n].setBackground(Color.WHITE);
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    //重新绘制掉落后方块的方法
    public void draw(int m,int n) {
        //定义变量
        int temp = 0x8000;
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((temp & rect) != 0) {
                    text[m][n].setBackground(Color.BLUE);
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //控制游戏暂停
        if (e.getKeyChar() == 'p') {
            //判断游戏是否结束
            if (!isrunning) {
                return;
            }

            pause_times++;

            //判断按下一次,暂停游戏
            if (pause_times == 1) {
                game_pause = true;
                label1.setText("游戏状态: 暂停中!");
            }
            //判断按下两次,继续游戏
            if (pause_times == 2) {
                game_pause = false;
                pause_times = 0;
                label1.setText("游戏状态: 正在进行中!");
            }
        }

        //控制方块进行变形
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            //判断游戏是否结束
            if (!isrunning) {
                return;
            }

            //判断游戏是否暂停
            if (game_pause) {
                return;
            }

            //定义变量,存储目前方块的索引
            int old;
            for (old = 0;old < allRect.length;old++) {
                //判断是否是当前方块
                if (rect == allRect[old]) {
                    break;
                }
            }

            //定义变量,存储变形后方块
            int next;

            //判断是方块
            if (old == 0 || old == 7 || old == 8 || old == 9) {
                return;
            }

            //清除当前方块
            clear(x,y);

            if (old == 1 || old == 2) {
                next = allRect[old == 1 ? 2 : 1];

                if (canTurn(next,x,y)) {
                    rect = next;
                }
            }

            if (old >= 3 && old <= 6) {
                next = allRect[old + 1 > 6 ? 3 : old + 1];

                if (canTurn(next,x,y)) {
                    rect = next;
                }
            }

            if (old == 10 || old == 11) {
                next = allRect[old == 10 ? 11 : 10];

                if (canTurn(next,x,y)) {
                    rect = next;
                }
            }

           if (old == 12 || old == 13) {
               next = allRect[old == 12 ? 13 : 12];

               if (canTurn(next,x,y)) {
                   rect = next;
               }
           }

           if (old >= 14 && old <= 17) {
               next = allRect[old + 1 > 17 ? 14 : old + 1];

               if (canTurn(next,x,y)) {
                   rect = next;
               }
           }

           if (old == 18 || old == 19) {
               next = allRect[old == 18 ? 19 : 18];

               if (canTurn(next,x,y)) {
                   rect = next;
               }
           }

           if (old == 20 || old == 21) {
               next = allRect[old == 20 ? 21 : 20];

               if (canTurn(next,x,y)) {
                   rect = next;
               }
           }

           //重新绘制变形后方块
            draw(x,y);
        }
    }

    //判断方块此时是否可以变形的方法
    public boolean canTurn(int a,int m,int n) {
        //创建变量
        int temp = 0x8000;
        //遍历整个方块
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((a & temp) != 0) {
                    if (data[m][n] == 1) {
                        return false;
                    }
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n -4;
        }
        //可以变形
        return true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //方块进行左移
        if (e.getKeyCode() == 37) {
            //判断游戏是否结束
            if (!isrunning) {
                return;
            }

            //判断游戏是否暂停
            if (game_pause) {
                return;
            }

            //方块是否碰到左墙壁
            if (y <= 1) {
                return;
            }

            //定义一个变量
            int temp = 0x8000;

            for (int i = x;i < x + 4;i++) {
                for (int j = y;j < y + 4;j++) {
                    if ((temp & rect) != 0) {
                        if (data[i][j-1] == 1) {
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }

            //首先清除目前方块
            clear(x,y);

            y--;

            draw(x,y);
        }

        //方块进行右移
        if (e.getKeyCode() == 39) {
            //判断游戏是否结束
            if (!isrunning) {
                return;
            }

            //判断游戏是否暂停
            if (game_pause) {
                return;
            }

            //定义变量
            int temp = 0x8000;
            int m = x;
            int n = y;

            //存储最右边的坐标值
            int num = 1;

            for (int i = 0;i < 4;i++) {
                for (int j = 0;j < 4;j++) {
                    if ((temp & rect) != 0) {
                        if (n > num) {
                            num = n;
                        }
                    }
                    n++;
                    temp >>= 1;
                }
                m++;
                n = n - 4;
            }

            //判断是否碰到右墙壁
            if (num >= (game_y-2)) {
                return;
            }

            //方块右移途中是否碰到别的方块
            temp = 0x8000;
            for (int i = x;i < x + 4;i++) {
                for (int j = y;j < y + 4;j++) {
                    if ((temp & rect) != 0) {
                        if (data[i][j+1] == 1) {
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }

            //清除当前方块
            clear(x,y);

            y++;

            draw(x,y);
        }

        //方块进行下落
        if (e.getKeyCode() == 40) {
            //判断游戏是否结束
            if (!isrunning) {
                return;
            }

            //判断游戏是否暂停
            if (game_pause) {
                return;
            }

            //判断方块是否可以下落
            if (!canFall(x,y)) {
                return;
            }

            clear(x,y);

            //改变方块的坐标
            x++;

            draw(x,y);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
