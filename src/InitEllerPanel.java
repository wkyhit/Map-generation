import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InitEllerPanel {

    private MazeGrid ellerGrid[][];
    private int rows;
    private int cols;
    private List<String> visited;   //保存图中所有已访问过块
    private List<String> willVisit; //保存待访问的当前块的邻接块

    private JPanel mazePanel;

    public InitEllerPanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        ellerGrid = new MazeGrid[rows][cols];

        willVisit = new ArrayList<String>();
        visited = new ArrayList<String>();

        mazePanel = new JPanel();
        mazePanel.setLayout(new GridLayout(rows, cols, 1, 1));
        mazePanel.setBackground(new Color(226, 143, 0));

        for (int i = 0; i < ellerGrid.length; i++)
            for (int j = 0; j < ellerGrid[i].length; j++) {
                //mark=true(偶数坐标)为通路, false(奇数坐标)为墙
                if (j % 2 == 0 && i % 2 == 0) {
                    ellerGrid[i][j] = new MazeGrid(true, 20, 20);
                } else {
                    ellerGrid[i][j] = new MazeGrid(false, 20, 20);
                }
            }

        //初始化入口
        ellerGrid[0][0].setVisited(true);
        ellerGrid[0][0].setPersonCome(true);
        ellerGrid[0][0].setStart(true);

        visited.add("0#0");

        //初始化出口
        ellerGrid[rows - 1][cols - 1].setEnd(true);

        /*Eller生成迷宫*/
        CreateEllerMap ellerMap = new CreateEllerMap();
        ellerMap.SetProperty(rows, cols, willVisit, visited, ellerGrid);
        ellerGrid = ellerMap.generateMap();
        /*Eller生成迷宫*/

        /*eller迷宫打印*/
        for (MazeGrid[] mazeGrids : ellerGrid) { //foreach 以每行遍历
            for (int j = 0; j < mazeGrids.length; j++) { //依次遍历每行各个方块

                mazeGrids[j].repaint();
                mazePanel.add(mazeGrids[j]);
                /*原先*/
//                centerPanel.add(mazeGrids[j]);
            }
        }

    }

    public MazeGrid[][] getMaze() {
        return this.ellerGrid;
    }

    public JPanel getMazePanel() {
        return this.mazePanel;
    }
}
