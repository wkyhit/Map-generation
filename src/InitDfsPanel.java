import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InitDfsPanel {

    private MazeGrid dfsGrid[][];
    private int rows;
    private int cols;
    private List<String> visited;   //保存图中所有已访问过块
    private List<String> willVisit; //保存待访问的当前块的邻接块

    private JPanel mazePanel;

    public InitDfsPanel(int rows, int cols) {

        this.rows = rows;
        this.cols = cols;
        dfsGrid = new MazeGrid[rows][cols];
        willVisit = new ArrayList<String>();
        visited = new ArrayList<String>();

        mazePanel = new JPanel();
        mazePanel.setLayout(new GridLayout(rows, cols, 1, 1));
        mazePanel.setBackground(new Color(0, 0, 0));

        for (int i = 0; i < dfsGrid.length; i++)
            for (int j = 0; j < dfsGrid[i].length; j++) {
                //mark=true(偶数坐标)为通路, false(奇数坐标)为墙
                if (j % 2 == 0 && i % 2 == 0) {
                    dfsGrid[i][j] = new MazeGrid(true, 20, 20);
                } else {
                    dfsGrid[i][j] = new MazeGrid(false, 20, 20);
                }
            }

        //初始化入口
        dfsGrid[0][0].setVisited(true);
        dfsGrid[0][0].setPersonCome(true);
        dfsGrid[0][0].setStart(true);

        visited.add("0#0");

        //初始化出口
        dfsGrid[rows - 1][cols - 1].setEnd(true);

        /*DFS生成迷宫*/
        CreateDfsMap dfsMap = new CreateDfsMap();//生成dfs对象
        dfsMap.SetProperty(rows, cols, willVisit, visited, dfsGrid);//把属性传入
        dfsGrid = dfsMap.generateMap();//生成迷宫
        /*DFS生成迷宫*/

        /*dfs迷宫打印*/
        for (MazeGrid[] mazeGrids : dfsGrid) { //foreach 以每行遍历
            for (int j = 0; j < mazeGrids.length; j++) { //依次遍历每行各个方块

                mazeGrids[j].repaint();
                mazePanel.add(mazeGrids[j]);
                /*原先*/
//                centerPanel.add(mazeGrids[j]);
            }
        }
    }

    public MazeGrid[][] getMaze() {
        return this.dfsGrid;
    }

    public JPanel getMazePanel() {
        return this.mazePanel;
    }


}
