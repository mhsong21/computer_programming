
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
//======================================================Don't modify below===============================================================//
enum PieceType {king, queen, bishop, knight, rook, pawn, none}
enum PlayerColor {black, white, none}
	

public class ChessBoard {
	private final JPanel gui = new JPanel(new BorderLayout(3, 3));
	private JPanel chessBoard;
	private JButton[][] chessBoardSquares = new JButton[8][8];
	private Piece[][] chessBoardStatus = new Piece[8][8];
	private ImageIcon[] pieceImage_b = new ImageIcon[7];
	private ImageIcon[] pieceImage_w = new ImageIcon[7];
	private JLabel message = new JLabel("Enter Reset to Start");

	ChessBoard(){
		initPieceImages();
		initBoardStatus();
		initializeGui();
	}
	
	public final void initBoardStatus(){
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++) chessBoardStatus[j][i] = new Piece();
		}
	}
	
	public final void initPieceImages(){
		pieceImage_b[0] = new ImageIcon(new ImageIcon("./img/king_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[1] = new ImageIcon(new ImageIcon("./img/queen_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[2] = new ImageIcon(new ImageIcon("./img/bishop_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[3] = new ImageIcon(new ImageIcon("./img/knight_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[4] = new ImageIcon(new ImageIcon("./img/rook_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[5] = new ImageIcon(new ImageIcon("./img/pawn_b.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_b[6] = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
		
		pieceImage_w[0] = new ImageIcon(new ImageIcon("./img/king_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[1] = new ImageIcon(new ImageIcon("./img/queen_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[2] = new ImageIcon(new ImageIcon("./img/bishop_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[3] = new ImageIcon(new ImageIcon("./img/knight_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[4] = new ImageIcon(new ImageIcon("./img/rook_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[5] = new ImageIcon(new ImageIcon("./img/pawn_w.png").getImage().getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
		pieceImage_w[6] = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
	}
	
	public ImageIcon getImageIcon(Piece piece){
		if(piece.color.equals(PlayerColor.black)){
			if(piece.type.equals(PieceType.king)) return pieceImage_b[0];
			else if(piece.type.equals(PieceType.queen)) return pieceImage_b[1];
			else if(piece.type.equals(PieceType.bishop)) return pieceImage_b[2];
			else if(piece.type.equals(PieceType.knight)) return pieceImage_b[3];
			else if(piece.type.equals(PieceType.rook)) return pieceImage_b[4];
			else if(piece.type.equals(PieceType.pawn)) return pieceImage_b[5];
			else return pieceImage_b[6];
		}
		else if(piece.color.equals(PlayerColor.white)){
			if(piece.type.equals(PieceType.king)) return pieceImage_w[0];
			else if(piece.type.equals(PieceType.queen)) return pieceImage_w[1];
			else if(piece.type.equals(PieceType.bishop)) return pieceImage_w[2];
			else if(piece.type.equals(PieceType.knight)) return pieceImage_w[3];
			else if(piece.type.equals(PieceType.rook)) return pieceImage_w[4];
			else if(piece.type.equals(PieceType.pawn)) return pieceImage_w[5];
			else return pieceImage_w[6];
		}
		else return pieceImage_w[6];
	}

	public final void initializeGui(){
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
	    JToolBar tools = new JToolBar();
	    tools.setFloatable(false);
	    gui.add(tools, BorderLayout.PAGE_START);
	    JButton startButton = new JButton("Reset");
	    startButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		initiateBoard();
	    	}
	    });
	    
	    tools.add(startButton);
	    tools.addSeparator();
	    tools.add(message);

	    chessBoard = new JPanel(new GridLayout(0, 8));
	    chessBoard.setBorder(new LineBorder(Color.BLACK));
	    gui.add(chessBoard);
	    ImageIcon defaultIcon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
	    Insets buttonMargin = new Insets(0,0,0,0);
	    for(int i=0; i<chessBoardSquares.length; i++) {
	        for (int j = 0; j < chessBoardSquares[i].length; j++) {
	        	JButton b = new JButton();
	        	b.addActionListener(new ButtonListener(i, j));
	            b.setMargin(buttonMargin);
	            b.setIcon(defaultIcon);
	            if((j % 2 == 1 && i % 2 == 1)|| (j % 2 == 0 && i % 2 == 0)) b.setBackground(Color.WHITE);
	            else b.setBackground(Color.gray);
	            b.setOpaque(true);
	            b.setBorderPainted(false);
	            chessBoardSquares[j][i] = b;
	        }
	    }

	    for (int i=0; i < 8; i++) {
	        for (int j=0; j < 8; j++) chessBoard.add(chessBoardSquares[j][i]);
	        
	    }
	}

	public final JComponent getGui() {
	    return gui;
	}
	
	public static void main(String[] args) {
	    Runnable r = new Runnable() {
	        @Override
	        public void run() {
	        	ChessBoard cb = new ChessBoard();
                JFrame f = new JFrame("Chess");
                f.add(cb.getGui());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);
                f.setResizable(false);
                f.pack();
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
	}
		
			//================================Utilize these functions========================================//	
	
	class Piece{
		PlayerColor color;
		PieceType type;
		
		Piece(){
			color = PlayerColor.none;
			type = PieceType.none;
		}
		Piece(PlayerColor color, PieceType type){
			this.color = color;
			this.type = type;
		}
	}
	
	public void setIcon(int x, int y, Piece piece){
		chessBoardSquares[y][x].setIcon(getImageIcon(piece));
		chessBoardStatus[y][x] = piece;
	}
	
	public Piece getIcon(int x, int y){
		return chessBoardStatus[y][x];
	}
	
	public void markPosition(int x, int y){
		chessBoardSquares[y][x].setBackground(Color.pink);
	}
	
	public void unmarkPosition(int x, int y){
		if((y % 2 == 1 && x % 2 == 1)|| (y % 2 == 0 && x % 2 == 0)) chessBoardSquares[y][x].setBackground(Color.WHITE);
		else chessBoardSquares[y][x].setBackground(Color.gray);
	}
	
	public void setStatus(String inpt){
		message.setText(inpt);
	}
	
	public void initiateBoard(){
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++) setIcon(i, j, new Piece());
		}
		setIcon(0, 0, new Piece(PlayerColor.black, PieceType.rook));
		setIcon(0, 1, new Piece(PlayerColor.black, PieceType.knight));
		setIcon(0, 2, new Piece(PlayerColor.black, PieceType.bishop));
		setIcon(0, 3, new Piece(PlayerColor.black, PieceType.queen));
		setIcon(0, 4, new Piece(PlayerColor.black, PieceType.king));
		setIcon(0, 5, new Piece(PlayerColor.black, PieceType.bishop));
		setIcon(0, 6, new Piece(PlayerColor.black, PieceType.knight));
		setIcon(0, 7, new Piece(PlayerColor.black, PieceType.rook));
		for(int i=0;i<8;i++){
			setIcon(1, i, new Piece(PlayerColor.black, PieceType.pawn));
			setIcon(6, i, new Piece(PlayerColor.white, PieceType.pawn));
		}
		setIcon(7, 0, new Piece(PlayerColor.white, PieceType.rook));
		setIcon(7, 1, new Piece(PlayerColor.white, PieceType.knight));
		setIcon(7, 2, new Piece(PlayerColor.white, PieceType.bishop));
		setIcon(7, 3, new Piece(PlayerColor.white, PieceType.queen));
		setIcon(7, 4, new Piece(PlayerColor.white, PieceType.king));
		setIcon(7, 5, new Piece(PlayerColor.white, PieceType.bishop));
		setIcon(7, 6, new Piece(PlayerColor.white, PieceType.knight));
		setIcon(7, 7, new Piece(PlayerColor.white, PieceType.rook));
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++) unmarkPosition(i, j);
		}
		onInitiateBoard();
	}
//======================================================Don't modify above==============================================================//	
	
//======================================================Implement below=================================================================//		
	
	public void routeChk(int x, int y, int i, int j, PlayerColor color){
		int isign = i;
		int jsign = j;
		PlayerColor enemyColor;
		if(color == PlayerColor.black)	enemyColor = PlayerColor.white;
		else	enemyColor = PlayerColor.black;
		while(x+i <= 7 && y+j <= 7 && x+i >= 0 && y+j >= 0){
			if(getIcon(x+i, y+j).color == enemyColor){
				markPosition(x+i,y+j);
				break;
			}
			else if(getIcon(x+i, y+j).color == color)	break;
			markPosition(x+i, y+j);
			i += isign; j += jsign;
		}
	}
	
	public void markAvailable(int x, int y, Piece p){
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++) unmarkPosition(i, j);
		}
		if(p.color == PlayerColor.white){
			if(p.type == PieceType.bishop){
				routeChk(x, y, 1, 1, PlayerColor.white);
				routeChk(x, y, 1, -1, PlayerColor.white);
				routeChk(x, y, -1, 1, PlayerColor.white);
				routeChk(x, y, -1, -1, PlayerColor.white);
			}
			else if(p.type == PieceType.king){
				for(int i = 0; i < 3; i++){
					for(int j = 0; j < 3; j++){
						if(x+i-1 >= 0 && x+i-1 <= 7 && y+j-1 >= 0 && y+j-1 <= 7)
							if(getIcon(x+i-1,y+j-1).type != PieceType.king && getIcon(x+i-1,y+j-1).color != PlayerColor.white)
								markPosition(x+i-1,y+j-1);
					}
				}
				unmarkPosition(x,y);
			}
			else if(p.type == PieceType.knight){
				int[] ik = {1, -1};
				int[] jk = {2, -2};
				
				for(int i = 0; i < 2; i++){
					for(int j = 0; j < 2; j++){
						if(x+ik[i] <= 7 && x+ik[i] >= 0 && y+jk[j] <= 7 && y+jk[j] >= 0)
							if(getIcon(x+ik[i],y+jk[j]).color != PlayerColor.white)
								markPosition(x+ik[i],y+jk[j]);
						if(x+jk[i] <= 7 && x+jk[i] >= 0 && y+ik[j] <= 7 && y+ik[j] >= 0)
							if(getIcon(x+jk[i],y+ik[j]).color != PlayerColor.white)
								markPosition(x+jk[i],y+ik[j]);
					}
				}
				
			}
			else if(p.type == PieceType.pawn){
				if(x-1 >= 0)if(getIcon(x-1,y).type == PieceType.none){
					markPosition(x-1,y);
					if(x == 6 && getIcon(x-2,y).type == PieceType.none)	markPosition(x-2,y);
				}
				if(x-1 >= 0 && y+1 <= 7)if(getIcon(x-1, y+1).color == PlayerColor.black)	markPosition(x-1,y+1);
				if(x-1 >= 0 && y-1 >= 0)if(getIcon(x-1, y-1).color == PlayerColor.black)	markPosition(x-1,y-1);
			}
			else if(p.type == PieceType.queen){
				routeChk(x, y, 1, 1, PlayerColor.white);
				routeChk(x, y, 1, -1, PlayerColor.white);
				routeChk(x, y, -1, 1, PlayerColor.white);
				routeChk(x, y, -1, -1, PlayerColor.white);
				routeChk(x, y, 1, 0, PlayerColor.white);
				routeChk(x, y, 0, 1, PlayerColor.white);
				routeChk(x, y, -1, 0, PlayerColor.white);
				routeChk(x, y, 0, -1, PlayerColor.white);
			}
			else if(p.type == PieceType.rook){
				routeChk(x, y, 1, 0, PlayerColor.white);
				routeChk(x, y, 0, 1, PlayerColor.white);
				routeChk(x, y, -1, 0, PlayerColor.white);
				routeChk(x, y, 0, -1, PlayerColor.white);
			}
		} 
		else{ // black
			if(p.type == PieceType.bishop){
				routeChk(x, y, 1, 1, PlayerColor.black);
				routeChk(x, y, 1, -1, PlayerColor.black);
				routeChk(x, y, -1, 1, PlayerColor.black);
				routeChk(x, y, -1, -1, PlayerColor.black);
			}
			else if(p.type == PieceType.king){
				for(int i = 0; i < 3; i++){
					for(int j = 0; j < 3; j++){
						if(x+i-1 >= 0 && x+i-1 <= 7 && y+j-1 >= 0 && y+j-1 <= 7)
							if(getIcon(x+i-1,y+j-1).type != PieceType.king && getIcon(x+i-1,y+j-1).color != PlayerColor.black)
								markPosition(x+i-1,y+j-1);
					}
				}
				unmarkPosition(x,y);
			}
			else if(p.type == PieceType.knight){
				int[] ik = {1, -1};
				int[] jk = {2, -2};
				
				for(int i = 0; i < 2; i++){
					for(int j = 0; j < 2; j++){
						if(x+ik[i] <= 7 && x+ik[i] >= 0 && y+jk[j] <= 7 && y+jk[j] >= 0)
							if(getIcon(x+ik[i],y+jk[j]).color != PlayerColor.black)
								markPosition(x+ik[i],y+jk[j]);
						if(x+jk[i] <= 7 && x+jk[i] >= 0 && y+ik[j] <= 7 && y+ik[j] >= 0)
							if(getIcon(x+jk[i],y+ik[j]).color != PlayerColor.black)
								markPosition(x+jk[i],y+ik[j]);
					}
				}
			}
			else if(p.type == PieceType.pawn){
				if(x+1 <= 7)if(getIcon(x+1,y).type == PieceType.none){
					markPosition(x+1,y);
					if(x == 1 && getIcon(x+2,y).type == PieceType.none)		markPosition(x+2,y);
				}
				if(x+1 <= 7 && y+1 <= 7)if(getIcon(x+1, y+1).color == PlayerColor.white)	markPosition(x+1,y+1);
				if(x+1 <= 7 && y-1 >= 0)if(getIcon(x+1, y-1).color == PlayerColor.white)	markPosition(x+1,y-1);
			}
			else if(p.type == PieceType.queen){
				routeChk(x, y, 1, 1, PlayerColor.black);
				routeChk(x, y, 1, -1, PlayerColor.black);
				routeChk(x, y, -1, 1, PlayerColor.black);
				routeChk(x, y, -1, -1, PlayerColor.black);
				routeChk(x, y, 1, 0, PlayerColor.black);
				routeChk(x, y, 0, 1, PlayerColor.black);
				routeChk(x, y, -1, 0, PlayerColor.black);
				routeChk(x, y, 0, -1, PlayerColor.black);
			}
			else if(p.type == PieceType.rook){
				routeChk(x, y, 1, 0, PlayerColor.black);
				routeChk(x, y, 0, 1, PlayerColor.black);
				routeChk(x, y, -1, 0, PlayerColor.black);
				routeChk(x, y, 0, -1, PlayerColor.black);
			}
		}
	}
	
	public boolean checkAvailable(PlayerColor turn){
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++) unmarkPosition(i, j);
		}
		int x = 0, y = 0; // king's position
		for(int i = 0; i <= 7; i++){
			for(int j = 0; j <= 7; j++){
				if(chessBoardStatus[j][i].type == PieceType.king && chessBoardStatus[j][i].color == turn){
					x = i; y = j;
				}
			}
		}
		PlayerColor enemy;
		if(turn == PlayerColor.black)	enemy = PlayerColor.white;
		else enemy = PlayerColor.black;
		for(int i = 0; i <= 7; i++){
			for(int j = 0; j <= 7; j++){
				if(chessBoardStatus[j][i].color == enemy){
					markAvailable(i, j, getIcon(i, j));
					if(chessBoardSquares[y][x].getBackground() == Color.pink){
						for(int m=0;m<8;m++){
							for(int n=0;n<8;n++) unmarkPosition(m, n);
						}
						return true;
					}
				}
			}
		}
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++) unmarkPosition(i, j);
		}
		return false;
	}
	
	public boolean checkmateAvailable(PlayerColor turn){
		for(int i=0;i<8;i++){ // i = x
			for(int j=0;j<8;j++){ // j = y
				if(chessBoardStatus[j][i].color == turn){
					Piece origP = getIcon(i, j);
					markAvailable(i, j, origP);
					for(int m=0;m<8;m++){ // m = x
						for(int n=0;n<8;n++){ // n = y
							if(chessBoardSquares[n][m].getBackground() == Color.pink){
								Piece enemyP = getIcon(m, n);
								setIcon(m, n, origP);
								setIcon(i, j, new Piece());
								
								if(!checkAvailable(turn)){
									setIcon(i, j, origP);
									setIcon(m, n, enemyP);
									return false;
								}
								setIcon(i, j, origP);
								setIcon(m, n, enemyP);
								markAvailable(i, j, origP);
							}
						}
					}
					for(int x=0;x<8;x++){
						for(int y=0;y<8;y++) unmarkPosition(x, y);
					}
				}
			}
		}
		return true;
	}
	
	int px;
	int py;
	boolean play = true;
	PlayerColor turnchk = PlayerColor.black;
	String checkmate = "";
	
	class ButtonListener implements ActionListener{
		int x;
		int y;
		
		ButtonListener(int x, int y){
			this.x = x;
			this.y = y;
		}
		public void actionPerformed(ActionEvent e) {	// Only modify here
			if(play){
				if(getIcon(x,y).color == turnchk){
					for(int i=0;i<8;i++){
						for(int j=0;j<8;j++) unmarkPosition(i, j);
					}
					markAvailable(x, y, getIcon(x, y)); //초기화 후 분홍색 색칠
				}
				else if(chessBoardSquares[y][x].getBackground() == Color.pink){// 누른게 분홍색일때 
					
					if(getIcon(x, y).type == PieceType.king)	play = false;
					setIcon(x, y, getIcon(px, py));
					setIcon(px, py, new Piece());
					for(int i=0;i<8;i++){
						for(int j=0;j<8;j++) unmarkPosition(i, j);
					}
					if(!play){
						setStatus(turnchk + " win!");	return;
					}
					if(turnchk == PlayerColor.black)	turnchk = PlayerColor.white;
					else	turnchk = PlayerColor.black;
					checkmate = "";
					if(checkAvailable(turnchk)){
						checkmate = " / check";
						if(checkmateAvailable(turnchk)){
							checkmate = " / checkmate";	
							play = false;
						}
					}
					setStatus(turnchk + "'s turn" + checkmate);
				}
				else{
					for(int i=0;i<8;i++){
						for(int j=0;j<8;j++) unmarkPosition(i, j);
					}
				}
				px = x; py = y;
			}
		}
		
	}
	
	void onInitiateBoard(){
		play = true;
		turnchk = PlayerColor.black;
		setStatus("black's turn");
	}
}