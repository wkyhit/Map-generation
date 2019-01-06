import java.awt.Graphics;
import java.awt.Canvas;
import java.awt.Color;

public class MazeGrid extends Canvas {

    private boolean mark;// 标记是否是通路，TRUE为通路，FALSE为不通  
    private boolean isVisited;// 标记是否是访问过的,这是在生成迷宫的时候判断的。
    private boolean isPersonCome;// 标记是否已经走过  
    private boolean isStart;// 判断是否为入口  
    private boolean isEnd;// 判断是否为出口  

    MazeGrid(boolean mark, int width, int height) { //单个方块图像生成
        this.mark = mark;
        this.setSize(width, height);
        if (mark) {
            this.setBackground(new Color(240, 240, 240)); //通路(mark=true) ：白色
        } else {
            this.setBackground(new Color(240, 240, 240));     //墙壁(mark=false)
        }
    }

    boolean isMark() {
        return mark;
    }

    void setMark() {
        this.mark = true;
    }

    public void paint(Graphics g) {
        if (this.mark) {
            if (this.isStart) {
                this.setBackground(new Color(115, 197, 179));
            } else if (this.isEnd) {
                this.setBackground(new Color(230, 141, 155));
            } else {
                this.setBackground(new Color(255, 255, 255));
            }
        } else {
            this.setBackground(new Color(155, 155, 155));
        }
        if (this.isPersonCome) {
            g.setColor(Color.BLACK);
            g.fillOval(2, 2, 15, 15);
        }

    }

    boolean noVisited() {
        return !isVisited;
    }

    void setVisited() {
        this.isVisited = true;
    }

    boolean isPersonCome() {
        return isPersonCome;
    }

    void setPersonCome(boolean isPersonCome) {
        this.isPersonCome = isPersonCome;
    }

    void setStart() {
        this.isStart = true;
    }

    void setEnd() {
        this.isEnd = true;
    }
}  