import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EightQueenManWithBanpei extends JFrame implements MouseListener, ActionListener {
    // マスの状態を表す変数
    public static int STATE_OK = 0;
    public static int STATE_QUEEN = 1;
    public static int STATE_BAD = 2;
    public static int STATE_BANPEI = 99;

    int[][] masu = new int[10][10];
    int oitaKazu;

    Container cPane;
    EQ_Panel panel;
    JPanel panelConsole;
    JLabel label;
    JButton button;

    public static void main(String[] args) {
        new EightQueenManWithBanpei();
    }


    EightQueenManWithBanpei() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cPane = getContentPane();
        cPane.setLayout(new BorderLayout());
        panel = new EQ_Panel(masu);
        panel.addMouseListener( this );
        cPane.add(panel, BorderLayout.CENTER);

        panelConsole = new JPanel();
        button = new JButton("New");
        button.addActionListener( this );
        panelConsole.add(button);
        label = new JLabel();
        label.setText( (oitaKazu+1) + "個目を置いてください");
        panelConsole.add(label);
        cPane.add(panelConsole, BorderLayout.SOUTH);

        this.setSize(340,400);
        this.setVisible(true);

        newGame();
    }

    void allCheck(int x, int y) {
        check( x, y,  1,  0 );	// 右
        check( x, y,  0,  1 );	// 下
        check( x, y, -1,  0 );	// 左
        check( x, y,  0, -1 );	// 上
        check( x, y,  1, -1 );	// 右上
        check( x, y,  1,  1 );	// 右下
        check( x, y, -1,  1 );	// 左下
        check( x, y, -1, -1 );	// 左上
    }

    void check(int x, int y, int dx, int dy) {
        int wx, wy;

        wx = x + dx;		wy = y + dy;
        System.out.println("masu[" + wx + "][" + wy +"] をチェックします");
        while( masu[wx][wy] != STATE_BANPEI ) {
            masu[wx][wy] = STATE_BAD;
            wx = wx + dx;	wy = wy + dy;
        }
    }

    void newGame() {
        // マスの状態を STATE_OK にする
        for (int y = 1; y < 9; ++y) {
            for (int x = 1; x < 9; ++x) {
                masu[x][y] = STATE_OK;
            }
        }
        // マスに番兵(STATE_BANPEI)を配置する
        for (int i = 0; i < 10; ++i) {
            masu[i][0] = STATE_BANPEI;
            masu[i][9] = STATE_BANPEI;
            masu[0][i] = STATE_BANPEI;
            masu[9][i] = STATE_BANPEI;
        }
        oitaKazu = 0;
        label.setText( (oitaKazu+1) + "個目を置いてください");
        repaint();
    }

    boolean owariCheck() {
        boolean kekka;

        kekka = true;
        for (int y = 1; y < 9; ++y) {
            for (int x = 1; x < 9; ++x) {
                if ( masu[x][y] == STATE_OK )
                    kekka = false;
            }
        }
        return kekka;
    }

    public void mouseEntered(MouseEvent evt) {}
    public void mouseExited(MouseEvent evt) {}
    public void mouseClicked(MouseEvent evt) {}
    public void mousePressed(MouseEvent evt) {}
    public void mouseReleased(MouseEvent evt) {
        int x, y;

        x = ( evt.getX() ) / 32;
        y = ( evt.getY() ) / 32;
        setTitle("(" + x + "," + y + ")");
        if (x >= 1 && x <= 8 && y >= 1 && y <= 8) {
            if ( masu[x][y] == STATE_OK ) {
                masu[x][y] = STATE_QUEEN;
                allCheck(x, y);
                oitaKazu++;
                repaint();
            }
            label.setText( (oitaKazu+1) + "個目を置いてください");
        }

        if ( owariCheck() ) {
            if ( oitaKazu == 8 )
                label.setText( "おめでとう！正解の一つです。");
            else
                label.setText( oitaKazu + "個 置けました");
        }
    }

    public void actionPerformed(ActionEvent evt) {
        if ( evt.getSource() == button ) {
            newGame();
        }
    }
}

class EQ_Panel extends JPanel {
    int [][] masu;

    Font qFont = new Font("Century", Font.PLAIN, 24);

    public EQ_Panel(int[][] masu) {
        this.masu = masu;
    }

    public void paint(Graphics g) {
        for (int y = 1; y < 9; ++y) {
            for (int x = 1; x < 9; ++x) {
                if ( masu[x][y] == EightQueenManWithBanpei.STATE_OK ) {			// 空欄
                    ;
                }
                else if ( masu[x][y] == EightQueenManWithBanpei.STATE_QUEEN ) {		// Queen
                    g.setColor( Color.GRAY );
                    g.fillRect(32*x, 32*y, 32, 32);
                    g.setColor( Color.YELLOW );
                    g.setFont( qFont );
                    g.drawString("Q", 32*x+7, 32*y+25);
                    g.setColor( Color.BLACK );
                }
                else if ( masu[x][y] == EightQueenManWithBanpei.STATE_BAD ) {		// 置けなくなった場所
                    g.setColor( Color.RED );
                    g.fillRect(32*x, 32*y, 32, 32);
                    g.setColor( Color.BLACK );
                }

                g.drawRect(32*x, 32*y, 32, 32);
            }
        }
    }
}