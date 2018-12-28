import java.util.List;

public class creatPrimMap {
    private int rows;
    private int cols;
    //    private List<String> willVisit; //保存待访问的当前通路集合的邻接白块
//    private List<String> visitedset;   //通路集合
    private List<String> wallList;
    private MazeGrid grid[][];


    public creatPrimMap() {
    }

    public void SetProperty(int rows, int cols, List<String> wallList, MazeGrid grid[][]) {
        this.rows = rows;
        this.cols = cols;
//        this.willVisit = willVisit;
//        this.visitedset = visited;
        this.wallList = wallList;
        this.grid = grid;
    }

    public MazeGrid[][] generateMap() {
        grid = createMap(grid, 0, 0);
        return grid;
    }

    public MazeGrid[][] createMap(MazeGrid mazeGrid[][], int x, int y) {

        int visitX = x;
        int visitY = y;
        int ajX;
        int ajY;

        //初始的mazeGrid[0][0].isvisited()=true
        //初始化墙表 将mazeGrid[0][0]的邻墙(右和下)加入墙列表中
        wallList.add((x + 1) + "#" + y);
        wallList.add(x + "#" + (y + 1));

        //        int count = 2;//邻墙数

        //循环条件：墙链不为空
        while (!wallList.isEmpty()) {
            //wallList中随机选出一个墙
            int visit = (int) (Math.random() * wallList.size()); //产生0~size的随机数
            String id = wallList.remove(visit);                  //将选择的墙从墙链中删除,并返回值给id
            //ajX ajY 为墙坐标
            ajX = Integer.parseInt(id.split("#")[0]);   //根据"#"分割字符串，将x,y坐标获取
            ajY = Integer.parseInt(id.split("#")[1]);   //将获取的x,y(string类型)转成int类型，并赋给ajX/ajY
            visitX = ajX;
            visitY = ajY;

            //将墙两边的格子放入通路集合
            if (((ajX - 1) >= 0) && mazeGrid[ajX - 1][ajY].isMark() && !mazeGrid[ajX - 1][ajY].isVisited()) {
                mazeGrid[ajX][ajY].setMark(true);              //将当前墙设为通路
                mazeGrid[ajX - 1][ajY].setVisited(true);       //将墙两边的通路格子纳入通路集合
                visitX = ajX - 1;                              //visitX为通路块坐标
            } else if (((ajX + 1) < cols) && mazeGrid[ajX + 1][ajY].isMark() && !mazeGrid[ajX + 1][ajY].isVisited()) {
                mazeGrid[ajX][ajY].setMark(true);              //将当前墙设为通路
                mazeGrid[ajX + 1][ajY].setVisited(true);       //将墙两边的通路格子纳入通路集合
                visitX = ajX + 1;
            } else if (((ajY - 1) >= 0) && mazeGrid[ajX][ajY - 1].isMark() && !mazeGrid[ajX][ajY - 1].isVisited()) {
                mazeGrid[ajX][ajY].setMark(true);              //将当前墙设为通路
                mazeGrid[ajX][ajY - 1].setVisited(true);       //将墙两边的通路格子纳入通路集合
                visitY = ajY - 1;
            } else if (((ajY + 1) < rows) && mazeGrid[ajX][ajY + 1].isMark() && !mazeGrid[ajX][ajY + 1].isVisited()) {
                mazeGrid[ajX][ajY].setMark(true);              //将当前墙设为通路
                mazeGrid[ajX][ajY + 1].setVisited(true);       //将墙两边的通路格子纳入通路集合
                visitY = ajY + 1;
            }

            //将新加入通路集合的格子的邻墙加入墙链中
            String isnWallList; //保存邻墙的坐标
            int findId;         //

            //条件：邻墙不是通路
            if (((visitX - 1) >= 0) && !mazeGrid[visitX - 1][visitY].isMark()) {
                isnWallList = (visitX - 1) + "#" + visitY;
                if (!wallList.contains(isnWallList)) { //如果墙链中无待存入的邻墙
                    wallList.add(isnWallList);
                } else {
                    findId = wallList.lastIndexOf(isnWallList);
                    wallList.remove(findId);            //如果墙链中已有该墙，则删除墙链中的墙
                }
            }

            if (((visitX + 1) < cols) && !mazeGrid[visitX + 1][visitY].isMark()) {
                isnWallList = (visitX + 1) + "#" + visitY;
                if (!wallList.contains(isnWallList)) { //如果墙链中无待存入的邻墙
                    wallList.add(isnWallList);
                } else {
                    findId = wallList.lastIndexOf(isnWallList);
                    wallList.remove(findId);            //如果墙链中已有该墙，则删除墙链中的墙
                }
            }

            if (((visitY - 1) >= 0) && !mazeGrid[visitX][visitY - 1].isMark()) {
                isnWallList = visitX + "#" + (visitY - 1);
                if (!wallList.contains(isnWallList)) { //如果墙链中无待存入的邻墙
                    wallList.add(isnWallList);
                } else {
                    findId = wallList.lastIndexOf(isnWallList);
                    wallList.remove(findId);            //如果墙链中已有该墙，则删除墙链中的墙
                }
            }

            if (((visitY + 1) < rows) && !mazeGrid[visitX][visitY + 1].isMark()) {
                isnWallList = visitX + "#" + (visitY + 1);
                if (!wallList.contains(isnWallList)) { //如果墙链中无待存入的邻墙
                    wallList.add(isnWallList);
                } else {
                    findId = wallList.lastIndexOf(isnWallList);
                    wallList.remove(findId);            //如果墙链中已有该墙，则删除墙链中的墙
                }
            }

        }//while

        return mazeGrid;
    }

}
