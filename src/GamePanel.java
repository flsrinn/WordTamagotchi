import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GamePanel extends JPanel {
    public ScorePanel scorePanel;
    private GameFrame gameFrame;
    private MonsterPanel monsterPanel;
    public GameGround gameGround;
    private JSplitPane hPane = new JSplitPane();
    private JSplitPane vPane = new JSplitPane();

    public GamePanel(GameFrame gameFrame, String playerName) {
	this.gameFrame = gameFrame;
	setLayout(new BorderLayout());
	splitPanel();
    }

    // GamePanel 영역 나누기
    private void splitPanel() {
	hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
	hPane.setDividerLocation(650);
	hPane.setDividerSize(0);
	add(hPane);

	vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
	vPane.setDividerLocation(350);

	// GameFrame으로부터 정보를 얻어와 각 패널 생성
	monsterPanel = new MonsterPanel(gameFrame.getStage(), gameFrame.getMonster());
	scorePanel = new ScorePanel(gameFrame.getLevel(), gameFrame.getStage(), gameFrame.getPlayerName(), monsterPanel, this);
	gameGround = new GameGround(scorePanel, gameFrame, gameFrame.getLevel(), gameFrame.getStage());
	// 오른쪽 위 점수 패널 / 오른쪽 아래 몬스터 패널
	// 왼쪽 본게임 패널
	vPane.setTopComponent(scorePanel);
	vPane.setBottomComponent(monsterPanel);
	hPane.setRightComponent(vPane);
	hPane.setLeftComponent(gameGround);
	vPane.setDividerSize(0);
    }

    // 게임 오버 화면
    public void gameOver(int score) {
	setLayout(null);
	remove(hPane);
	remove(vPane);

	JLabel ending = new JLabel("Game Over!");
	ending.setFont(new Font("DungGeunMo", Font.PLAIN, 60));
	ending.setSize(400, 60);
	ending.setLocation(300, 100);
	add(ending);

	// 최종 점수 출력
	JLabel gameOverScore = new JLabel("SCORE " + Integer.toString(score));
	gameOverScore.setFont(new Font("DungGeunMo", Font.PLAIN, 45));
	gameOverScore.setSize(300, 45);
	gameOverScore.setLocation(350, 200);
	add(gameOverScore);

	// 게임 재시작 버튼
	JButton restartButton = new JButton("RESTART");
	restartButton.setLocation(340, 400);
	restartButton.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
	restartButton.setOpaque(false);
	restartButton.setBackground(new Color(0, 0, 0, 0));
	restartButton.setSize(200, 40);
	restartButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		gameFrame.toStartPanel();
	    }
	});
	add(restartButton);

	// 게임 종료 버튼
	JButton exitButton = new JButton("EXIT");
	exitButton.setLocation(340, 460);
	exitButton.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
	exitButton.setOpaque(false);
	exitButton.setBackground(new Color(0, 0, 0, 0));
	exitButton.setSize(200, 40);
	exitButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		System.exit(0);
	    }
	});
	add(exitButton);
	revalidate();
	repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	ImageIcon icon = new ImageIcon("background/startBackground.jpg");
	Image backgroundImg = icon.getImage();
	g.drawImage(backgroundImg, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
