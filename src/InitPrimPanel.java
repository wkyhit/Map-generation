import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InitPrimPanel {

    private MazeGrid primGrid[][];
    private int rows;
    private int cols;
    private List<String> visited;   //保存图中所有已访问过块
    //    private List<String> willVisit; //保存待访问的当前块的邻接块
    private List<String> wallList; //prim算法墙链

    private JPanel mazePanel;

    public InitPrimPanel(int rows, int cols) {

        this.rows = rows;
        this.cols = cols;
        primGrid = new MazeGrid[rows][cols];
        wallList = new LinkedList<>();
        visited = new ArrayList<>();

        mazePanel = new JPanel();
        mazePanel.setLayout(new GridLayout(rows, cols, 1, 1));
        mazePanel.setBackground(new Color(0, 15, 255));

        //初始化迷宫图的各单元
        for (int i = 0; i < primGrid.length; i++)
            for (int j = 0; j < primGrid[i].length; j++) {
                //mark=true(偶数坐标)为通路, false(奇数坐标)为墙
                if (j % 2 == 0 && i % 2 == 0) {
                    primGrid[i][j] = new MazeGrid(true, 20, 20);//初始化prim
                } else {
                    primGrid[i][j] = new MazeGrid(false, 20, 20);//初始化prim
                }
            }

        primGrid[0][0].setVisited(true);
        primGrid[0][0].setPersonCome(true);
        primGrid[0][0].setStart(true);

        visited.add("0#0");

        //初始化出口
        primGrid[rows - 1][cols - 1].setEnd(true);

        /*Prim生成迷宫*/
        CreatePrimMap primMap = new CreatePrimMap();
        primMap.SetProperty(rows, cols, wallList, primGrid);
        primGrid = primMap.generateMap();
        /*Prim生成迷宫*/

        /*prim迷宫打印*/
        for (MazeGrid[] mazeGrids : primGrid) { //foreach 以每行遍历
            for (int j = 0; j < mazeGrids.length; j++) { //依次遍历每行各个方块

                mazeGrids[j].repaint();
                mazePanel.add(mazeGrids[j]);
                /*原先*/
//                centerPanel.add(mazeGrids[j]);
            }
        }
    }

    public MazeGrid[][] getMaze() {
        return this.primGrid;
    }

    public JPanel getMazePanel() {
        return this.mazePanel;
    }
}
