package ����˹����;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Tertris extends JFrame implements KeyListener{
    //��Ϸ������26,����12
    private static final int game_x = 26;
    private static final int game_y = 12;
    //�ı�������
    JTextArea[][] text;
    //��ά����
    int[][] data;
    //��ʾ��Ϸ״̬�ı�ǩ
    JLabel label1;
    //��ʾ��Ϸ�����ı�ǩ
    JLabel label;
    //�����ж���Ϸ�Ƿ����
    boolean isrunning;
    //���ڴ洢���еķ��������
    int[] allRect;
    //���ڴ洢��ǰ����ı���
    int rect;
    //�̵߳�����ʱ��
    int time = 1000;
    //��ʾ��������
    int x, y;
    //�ñ������ڼ���÷�
    int score = 0;
    //����һ����־����,�����ж���Ϸ�Ƿ���ͣ
    boolean game_pause = false;
    //����һ���������ڼ�¼������ͣ���Ĵ���
    int pause_times = 0;

    public void initWindow() {
        //���ô��ڴ�С
        this.setSize(600,850);
        //���ô����Ƿ�ɼ�
        this.setVisible(true);
        //���ô��ھ���
        this.setLocationRelativeTo(null);
        //�����ͷŴ���
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //���ô��ڴ�С���ɱ�
        this.setResizable(false);
        //���ñ���
        this.setTitle("�ҵĶ���˹������Ϸ");
    }

    //��ʼ����Ϸ����
    public void initGamePanel() {
        JPanel game_main = new JPanel();
        game_main.setLayout(new GridLayout(game_x,game_y,1,1));
        //��ʼ�����
        for (int i = 0 ; i < text.length ; i++) {
            for (int j = 0 ; j < text[i].length ;j++) {
                //�����ı����������
                text[i][j] = new JTextArea(game_x,game_y);
                //�����ı���ı�����ɫ
                text[i][j].setBackground(Color.WHITE);
                //��Ӽ��̼����¼�
                text[i][j].addKeyListener(this);
                //��ʼ����Ϸ�߽�
                if (j == 0 || j == text[i].length-1 || i == text.length-1) {
                    text[i][j].setBackground(Color.MAGENTA);
                    data[i][j] = 1;
                }
                //�����ı����򲻿ɱ༭
                text[i][j].setEditable(false);
                //�ı�������ӵ��������
                game_main.add(text[i][j]);
            }
        }
        //��ӵ�������
        this.setLayout(new BorderLayout());
        this.add(game_main,BorderLayout.CENTER);
    }

    //��ʼ����Ϸ��˵�����
    public void initExplainPanel() {
        //������Ϸ����˵�����
        JPanel explain_left = new JPanel();
        //������Ϸ����˵�����
        JPanel explain_right = new JPanel();

        explain_left.setLayout(new GridLayout(4,1));
        explain_right.setLayout(new GridLayout(2,1));
        //��ʼ����˵�����

        //����˵�����,���˵������
        explain_left.add(new JLabel("���ո��,�������"));
        explain_left.add(new JLabel("�����ͷ,��������"));
        explain_left.add(new JLabel("���Ҽ�ͷ,��������"));
        explain_left.add(new JLabel("���¼�ͷ,��������"));
        //���ñ�ǩ������Ϊ��ɫ����
        label1.setForeground(Color.RED);
        //����Ϸ״̬��ǩ,��Ϸ������ǩ,��ӵ���˵�����
        explain_right.add(label);
        explain_right.add(label1);
        //����˵�������ӵ����ڵ����
        this.add(explain_left,BorderLayout.WEST);
        //����˵�������ӵ����ڵ��Ҳ�
        this.add(explain_right,BorderLayout.EAST);
    }

    public Tertris() {
        text = new JTextArea[game_x][game_y];
        data = new int[game_x][game_y];
        //��ʼ����ʾ��Ϸ״̬�ı�ǩ
        label1 = new JLabel("��Ϸ״̬: ������Ϸ��!");
        //��ʼ����ʾ��Ϸ�����ı�ǩ
        label = new JLabel("��Ϸ�÷�Ϊ: 0");
        initGamePanel();
        initExplainPanel();
        initWindow();
        //��ʼ����ʼ��Ϸ�ı�־
        isrunning = true;
        //��ʼ����ŷ��������
        allRect = new int[]{0x00cc,0x8888,0x000f,0x888f,0xf888,0xf111,0x111f,0x0eee,0xffff,0x0008,0x0888,0x000e,0x0088,0x000c,0x08c8,0x00e4
        ,0x04c4,0x004e,0x08c4,0x006c,0x04c8,0x00c6};
    }

    public static void main(String[] args) {
        Tertris tertris = new Tertris();
        tertris.game_begin();
    }

    //��ʼ��Ϸ�ķ���
    public void game_begin() {
        while (true){
            //�ж���Ϸ�Ƿ����
            if (!isrunning) {
                break;
            }

            //������Ϸ
            game_run();
        }
        //�ڱ�ǩλ����ʾ"��Ϸ����"
        label1.setText("��Ϸ״̬: ��Ϸ����!");
    }

    //����������䷽����״�ķ���
    public void ranRect() {
        Random random = new Random();

        rect = allRect[random.nextInt(22)];
    }

    //��Ϸ���еķ���
    public void game_run() {
        ranRect();
        //��������λ��
        x = 0;
        y = 5;

        for (int i = 0;i < game_x;i++) {
            try {
                Thread.sleep(time);

                if (game_pause) {
                    i--;
                } else {
                    //�жϷ����Ƿ��������
                    if (!canFall(x,y)) {
                        //��data��Ϊ1,��ʾ�з���ռ��
                        changData(x,y);
                        //ѭ������4��,���Ƿ����п�������
                        for (int j = x;j < x + 4;j++) {
                            int sum = 0;

                            for (int k = 1;k <= (game_y-2);k++) {
                                if (data[j][k] == 1) {
                                    sum++;
                                }
                            }

                            //�ж��Ƿ���һ�п��Ա�����
                            if (sum == (game_y-2)) {
                                //����j��һ��
                                removeRow(j);
                            }
                        }
                        //�ж���Ϸ�Ƿ�ʧ��
                        for (int j = 1;j <= (game_y-2);j++) {
                            if (data[3][j] == 1) {
                                isrunning = false;
                                break;
                            }
                        }
                        break;
                    } else {
                        //����+1
                        x++;
                        //��������һ��
                        fall(x,y);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //�жϷ����Ƿ���Լ�������ķ���
    public boolean canFall(int m,int n) {
        //����һ������
        int temp = 0x8000;
        //����4 * 4����
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((temp & rect) != 0) {
                    //�жϸ�λ�õ���һ���Ƿ��з���
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
        //��������
        return true;
    }

    //�ı䲻���½��ķ����Ӧ�������ֵ�ķ���
    public void changData(int m,int n) {
        //����һ������
        int temp = 0x8000;
        //��������4 * 4�ķ���
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

    //�Ƴ�ĳһ�е����з���,�����Ϸ������ķ���
    public void removeRow(int row) {
        int temp = 100;
        for (int i = row;i >= 1;i--) {
            for (int j = 1;j <= (game_y-2);j++) {
                //���и���
                data[i][j] = data[i-1][j];
            }
        }
        //ˢ����Ϸ����
        reflesh(row);

        //�������
        if (time > temp) {
            time -= temp;
        }

        score += temp;

        //��ʾ�仯��ķ���
        label.setText("��Ϸ�÷�Ϊ: " + score);
    }

    //ˢ���Ƴ�ĳһ�к����Ϸ����ķ���
    public void reflesh(int row) {
        //����row�����ϵ���Ϸ����
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

    //�������µ���һ��ķ���
    public void fall(int m,int n) {
        if (m > 0) {
            //�����һ�㷽��
            clear(m-1,n);
        }
        //���»��Ʒ���
        draw(m,n);
    }

    //�����������,��һ������ɫ�ĵط��ķ���
    public void clear(int m,int n) {
        //�������
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

    //���»��Ƶ���󷽿�ķ���
    public void draw(int m,int n) {
        //�������
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
        //������Ϸ��ͣ
        if (e.getKeyChar() == 'p') {
            //�ж���Ϸ�Ƿ����
            if (!isrunning) {
                return;
            }

            pause_times++;

            //�жϰ���һ��,��ͣ��Ϸ
            if (pause_times == 1) {
                game_pause = true;
                label1.setText("��Ϸ״̬: ��ͣ��!");
            }
            //�жϰ�������,������Ϸ
            if (pause_times == 2) {
                game_pause = false;
                pause_times = 0;
                label1.setText("��Ϸ״̬: ���ڽ�����!");
            }
        }

        //���Ʒ�����б���
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            //�ж���Ϸ�Ƿ����
            if (!isrunning) {
                return;
            }

            //�ж���Ϸ�Ƿ���ͣ
            if (game_pause) {
                return;
            }

            //�������,�洢Ŀǰ���������
            int old;
            for (old = 0;old < allRect.length;old++) {
                //�ж��Ƿ��ǵ�ǰ����
                if (rect == allRect[old]) {
                    break;
                }
            }

            //�������,�洢���κ󷽿�
            int next;

            //�ж��Ƿ���
            if (old == 0 || old == 7 || old == 8 || old == 9) {
                return;
            }

            //�����ǰ����
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

           //���»��Ʊ��κ󷽿�
            draw(x,y);
        }
    }

    //�жϷ����ʱ�Ƿ���Ա��εķ���
    public boolean canTurn(int a,int m,int n) {
        //��������
        int temp = 0x8000;
        //������������
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
        //���Ա���
        return true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //�����������
        if (e.getKeyCode() == 37) {
            //�ж���Ϸ�Ƿ����
            if (!isrunning) {
                return;
            }

            //�ж���Ϸ�Ƿ���ͣ
            if (game_pause) {
                return;
            }

            //�����Ƿ�������ǽ��
            if (y <= 1) {
                return;
            }

            //����һ������
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

            //�������Ŀǰ����
            clear(x,y);

            y--;

            draw(x,y);
        }

        //�����������
        if (e.getKeyCode() == 39) {
            //�ж���Ϸ�Ƿ����
            if (!isrunning) {
                return;
            }

            //�ж���Ϸ�Ƿ���ͣ
            if (game_pause) {
                return;
            }

            //�������
            int temp = 0x8000;
            int m = x;
            int n = y;

            //�洢���ұߵ�����ֵ
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

            //�ж��Ƿ�������ǽ��
            if (num >= (game_y-2)) {
                return;
            }

            //��������;���Ƿ�������ķ���
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

            //�����ǰ����
            clear(x,y);

            y++;

            draw(x,y);
        }

        //�����������
        if (e.getKeyCode() == 40) {
            //�ж���Ϸ�Ƿ����
            if (!isrunning) {
                return;
            }

            //�ж���Ϸ�Ƿ���ͣ
            if (game_pause) {
                return;
            }

            //�жϷ����Ƿ��������
            if (!canFall(x,y)) {
                return;
            }

            clear(x,y);

            //�ı䷽�������
            x++;

            draw(x,y);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
