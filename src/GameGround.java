import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

public class GameGround extends JPanel {
    private ScorePanel scorePanel;
    private TextSource textSource = null;
    private JTextField textInput = new JTextField(20);
    private Vector<JLabel> label = new Vector<JLabel>();
    private ImageIcon iconImage[] = new ImageIcon[5];
    private ImageIcon icon;
    private Image backgroundImg;
    private int x;
    private int wordIndex = 0;
    private int level;
    private int stage = 1;
    public GameThread gameth;
    public WordThread wordth;
    private DoubleScoreThread dst;
    private GameFrame gameFrame;
    // 단어 2배 이벤트 발생 시 true
    private boolean isDouble = false;

    public GameGround(ScorePanel scorePanel, GameFrame gameFrame, int level, int stage) {
	textSource = new TextSource(this);
	this.scorePanel = scorePanel;
	this.gameFrame = gameFrame;
	this.level = level;
	this.stage = stage;
	setLayout(null);

	for (int i = 0; i < iconImage.length; i++) {
	    iconImage[i] = new ImageIcon("image/item/" + (i + 1) + ".png");
	}
	newWord(level);
	runGame();
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	if (stage == 1) {
	    icon = new ImageIcon("image/background/gameBackground1.jpg");
	    g.setColor(new Color(248, 219, 234));
	} else if (stage == 2) {
	    icon = new ImageIcon("image/background/gameBackground2.jpg");
	    g.setColor(new Color(14, 13, 89));
	} else {
	    icon = new ImageIcon("image/background/gameBackground3.jpg");
	    g.setColor(new Color(148, 96, 241));
	}
	backgroundImg = icon.getImage();
	g.drawImage(backgroundImg, 0, 0, this.getWidth(), this.getHeight(), this);
	g.fillRect(0, 480, this.getWidth(), this.getHeight());
    }

    // 단어를 생성하는 메소드
    public void newWord(int level) {
	label.add(new JLabel());
	String word = textSource.next();
	// 쉬움 단계일 경우 단어 길이 5자 이하로 제한
	if (level == 1) {
	    while (true) {
		if (word.length() > 6) {
		    word = textSource.next();
		} else
		    break;
	    }
	}

	// 어려움 단계일 경우 단어 길이 11자 이하로 제한
	else {
	    while (true) {
		if (word.length() > 12) {
		    word = textSource.next();
		} else
		    break;
	    }
	}

	int r = (int) (Math.random() * 100) + 1;
	int imageIndex = -1;
	// 60% 확률로 사과 아이콘
	if (r <= 20)
	    imageIndex = 0;
	// 15% 확률로 고기 아이콘
	else if (r <= 40)
	    imageIndex = 1;
	// 10% 확률로 시계 아이콘
	else if (r <= 60)
	    imageIndex = 2;
	// 10% 확률로 생명 아이콘
	else if (r <= 80)
	    imageIndex = 3;
	// 5% 확률로 컵케이크 아이콘
	else
	    imageIndex = 4;

	Image image = iconImage[imageIndex].getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	ImageIcon icon = new ImageIcon(image);

	if (level == 1) {
	    x = (int) (Math.random() * 500) + 40;
	} else {
	    x = (int) (Math.random() * 450) + 40;
	}
	// 랜덤한 단어를 만들고
	// 아이템 이미지를 아이콘에 달기
	JLabel wordLabel = label.get(wordIndex);
	wordLabel.setText(word);
	wordLabel.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
	wordLabel.setSize(200, 30);
	wordLabel.setLocation(x, 20);
	if (stage >= 2) {
	    wordLabel.setForeground(Color.white);
	}
	wordLabel.setName(Integer.toString(imageIndex));
	wordLabel.setIcon(icon);
	add(wordLabel);
	wordIndex++;
    }

    // 단어 게임 진행 메소드
    private void runGame() {
	// 단어를 입력할 텍스트 필드
	if (stage >= 2) {
	    textInput.setForeground(Color.white);
	}
	textInput.setSize(300, 30);
	textInput.setLocation(170, 485);
	textInput.setBackground(new Color(14, 13, 89));
	textInput.setFont(new Font("DungGeunMo", Font.PLAIN, 18));
	textInput.setFocusable(true);
	textInput.setOpaque(false);
	add(textInput);
	textInput.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		boolean found = false;
		JTextField tf = (JTextField) e.getSource();
		String text = tf.getText();
		for (int i = 0; i < label.size(); i++) {
		    // 화면에 있는 단어와 입력한 단어가 일치할 경우
		    if (text.equals(label.get(i).getText())) {
			boolean stageClear = false;
			int score = 0;
			String imageIndex = label.get(i).getName();
			// 입력한 단어의 아이콘에 따라 결정
			switch (imageIndex) {
			// 사과 아이콘
			case "0":
			    if (isDouble)
				score = 10;
			    else
				score = 5;
			    break;
			// 고기 아이콘
			case "1":
			    if (isDouble)
				score = 30;
			    else
				score = 15;
			    break;
			// 시계 아이콘
			case "2":
			    gameth.stopRain(false);
			    wordth.stopWord(false);
			    break;
			// 생명 아이콘
			case "3":
			    scorePanel.addLife();
			    break;
			// 컵케이크 아이콘
			case "4":
			    dst.doubleScore();
			    break;
			}
			stageClear = scorePanel.increase(score, stage);
			// 단어 숨기기
			label.get(i).setVisible(false);
			found = true;
			// 스테이지를 클리어했을 경우 스레드를 멈춤
			if (stageClear) {
			    gameth.interrupt();
			    wordth.interrupt();
			    gameFrame.toNextStage();
			}
		    }
		}

		// 화면에 있는 단어와 입력한 단어가 일치하지 않을 경우
		if (!found) {
		    boolean isOver;
		    isOver = scorePanel.decrease(stage);
		    if (isOver) {
			gameth.interrupt();
			wordth.interrupt();
		    }
		}
		tf.setText("");
	    }
	});
	// 스레드 작동 시작
	gameth = new GameThread(scorePanel, stage);
	gameth.start();
	wordth = new WordThread(stage);
	wordth.start();
	dst = new DoubleScoreThread();
	dst.start();
    }

    // 단어를 밑으로 내리는 스레드
    class GameThread extends Thread {
	private boolean stopFlag = false;
	private ScorePanel scorePanel;
	private int stage;
	private boolean pause = false;
	private JLabel pauseInfo;

	public GameThread(ScorePanel scorePanel, int stage) {
	    this.scorePanel = scorePanel;
	    this.stage = stage;
	}

	// 내려오는 단어를 멈추는 메소드
	public void stopRain(boolean pause) {
	    this.pause = pause;
	    stopFlag = true;
	    // pause가 true일 경우 게임을 일시정지 시킴
	    if (pause == true) {
		remove(textInput);
		pauseInfo = new JLabel("PAUSED");
		pauseInfo.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		pauseInfo.setSize(80, 20);
		pauseInfo.setLocation(300, 490);
		pauseInfo.setForeground(Color.WHITE);
		add(pauseInfo);
		repaint();
	    }
	}

	// 단어가 다시 내려오게 만드는 메소드
	synchronized public void resumeRain() {
	    stopFlag = false;
	    if (pause) {
		add(textInput);
		remove(pauseInfo);
		pause = false;
		repaint();
	    }
	}

	// 내려오는 단어 5초간 멈추는 메소드
	synchronized private void checkWait() {
	    if (stopFlag && !pause) {
		try {
		    Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		resumeRain();
	    }
	}

	@Override // 스레드 무한루프
	public void run() {
	    while (true) {
		checkWait();
		if (!stopFlag) {
		    for (int i = 0; i < label.size(); i++) {
			// 단어가 바닥까지 내려왔을 경우
			if (label.get(i).getY() > 420 && label.get(i).isVisible()) {
			    label.get(i).setText("");
			    label.get(i).setVisible(false);
			    boolean isOver = scorePanel.decrease(stage);
			    // 게임오버일 경우 스레드를 멈춤
			    if (isOver) {
				wordth.interrupt();
				gameth.interrupt();
			    }
			} else {
			    label.get(i).setLocation(label.get(i).getX(), label.get(i).getY() + 10);
			}
		    }
		}

		try {
		    // 스테이지 1일 경우 0.5초 간격
		    if (stage == 1) {
			Thread.sleep(500);
		    }
		    // 스테이지 2일 경우 0.4초 간격
		    else if (stage == 2) {
			Thread.sleep(400);
		    }
		    // 스테이지 3일 경우 0.35초 간격
		    else {
			Thread.sleep(350);
		    }
		} catch (InterruptedException e) {
		    stopFlag = true;
		    return;
		}
	    }
	}
    }

    // 단어를 새로 만드는 스레드
    class WordThread extends Thread {
	private boolean stopFlag = false;
	private int stage;
	private boolean pause = false;

	public WordThread(int stage) {
	    this.stage = stage;
	}

	// 단어 생성을 멈추는 메소드
	public void stopWord(boolean pause) {
	    this.pause = pause;
	    stopFlag = true;
	}

	// 단어 생성을 다시 시작하는 메소드
	synchronized public void resumeWord() {
	    stopFlag = false;
	    if (pause)
		pause = false;
	}

	// 단어 생성 5초간 멈추는 메소드
	synchronized private void checkWait() {
	    if (stopFlag && !pause) {
		try {
		    Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		resumeWord();
	    }
	}

	@Override
	public void run() {
	    try {
		while (true) {
		    checkWait();
		    if (!stopFlag) {
			// 쉬움 단계
			if (level == 1) {
			    // 스테이지 1일 경우 2.5초 간격
			    if (stage == 1) {
				Thread.sleep(2500);
			    }
			    // 스테이지 2일 경우 2.35초 간격
			    else if (stage == 2) {
				Thread.sleep(2350);
			    }
			    // 스테이지 3일 경우 2.2초 간격
			    else {
				Thread.sleep(2200);
			    }
			}
			// 어려움 단계
			else {
			    // 스테이지 1일 경우 2.2초 간격
			    if (stage == 1) {
				Thread.sleep(2200);
			    }
			    // 스테이지 2일 경우 2초 간격
			    else if (stage == 2) {
				Thread.sleep(2000);
			    }
			    // 스테이지 3일 경우 1.8초 간격
			    else {
				Thread.sleep(1800);
			    }
			}
			
			// 새 단어 생성
			synchronized (label) {
			    newWord(level);
			}
		    }
		}
	    } catch (InterruptedException e) {
		stopFlag = true;
		return;
	    }
	}
    }

    // 10초동안 점수 2배 스레드
    class DoubleScoreThread extends Thread {
	private boolean stopFlag = true;
	private int count = 0;
	private JLabel leftTime = new JLabel();

	// 스레드를 wait 상태로 만드는 메소드
	synchronized public void stopDoubleScore() {
	    stopFlag = true;
	    leftTime.setVisible(false);
	    isDouble = false;
	    try {
		wait();
	    } catch (InterruptedException e) {
	    }
	}

	// 스레드를 Runnable 상태로 만드는 메소드
	synchronized public void doubleScore() {
	    stopFlag = false;
	    leftTime.setVisible(true);
	    isDouble = true;
	    count += 10;
	    notify();
	}

	// 10초간 점수를 2배로 처리하게 만드는 메소드
	synchronized private void checkWait() {
	    if (!stopFlag) {
		try {
		    remove(leftTime);
		    // 점수 2배 효과의 남은 시간을 알려줌
		    leftTime.setText("SCOREx2 " + Integer.toString(count) + "sec");
		    leftTime.setForeground(new Color(253, 135, 135));
		    leftTime.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
		    leftTime.setSize(200, 20);
		    leftTime.setLocation(30, 490);
		    add(leftTime);
		    revalidate();
		    repaint();
		    count--;
		    Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	    }
	}

	@Override
	public void run() {
	    while (true) {
		checkWait();
		// 10초가 지나면 스레드를 wait 상태로 바꿈
		if (count == 0) {
		    stopDoubleScore();
		}

	    }
	}
    }
}