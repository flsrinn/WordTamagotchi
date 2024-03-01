import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class NextStage extends JPanel {
    private BarLabel bar = new BarLabel();
    private GameFrame gameFrame;
    private Color purple = new Color(14, 13, 89);
    private NextStage nextStage = this;
    private int stage;
    private BarThread bt = new BarThread(bar);
    private JButton nextButton = new JButton("Next Stage");
    private int maxBarSize = 500;
    private Clip clip;

    public NextStage(GameFrame gameFrame, int stage) {
	this.gameFrame = gameFrame;
	this.stage = stage;
	setLayout(null);
	setBackground(new Color(255, 171, 228));
	bar.setOpaque(true);
	bar.setBackground(purple);
	bar.setLocation(190, 150);
	bar.setSize(500, 70);
	add(bar);

	JLabel info = new JLabel("! 스페이스바 연타 !");
	info.setFont(new Font("DungGeunMo", Font.PLAIN, 30));
	info.setSize(300, 30);
	info.setLocation(290, 300);
	add(info);

	// 스페이스바를 누르면 fill() 메소드 실행
	setFocusable(true);
	addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SPACE) {
		    bar.fill();
		}
	    }
	});

	// 누르면 다음 스테이지로 넘어가는 버튼
	nextButton.setLocation(330, 400);
	nextButton.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
	nextButton.setOpaque(false);
	nextButton.setBackground(null);
	nextButton.setSize(200, 40);
	nextButton.setVisible(false);
	nextButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// 스테이지 3에서 넘어가는 경우 게임 클리어 화면 출력
		if (stage == 3) {
		    loadAudio("audio/gameClear.wav");
		    gameClear();
		} else {
		    // 게임 정보 재설정(현 stage+1) 후 다음 스테이지로 넘어감
		    gameFrame.setGameInfo(gameFrame.getLevel(), gameFrame.getStage() + 1, gameFrame.getMonster(),
			    gameFrame.getPlayerName());
		    gameFrame.toGamePanel();
		}
	    }
	});
	add(nextButton);

	// 바 스레드 실행
	bt.start();
	setVisible(true);
    }

    // 게임 클리어 메소드
    public void gameClear() {
	removeAll();
	JLabel gameClear = new JLabel("GAME CLEAR !");
	gameClear.setFont(new Font("DungGeunMo", Font.PLAIN, 40));
	gameClear.setSize(260, 40);
	gameClear.setLocation(310, 100);
	add(gameClear);

	// 행복한 아기 몬스터와 어른 몬스터 출력
	int monsterIndex = gameFrame.getMonster();
	ImageIcon babyIcon = new ImageIcon("image/monster/happybaby" + (monsterIndex + 1) + ".png");
	ImageIcon adultIcon = new ImageIcon("image/monster/happyadult" + (monsterIndex + 1) + ".png");
	JLabel babyImg = new JLabel(new ImageIcon(babyIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
	JLabel adultImg = new JLabel(
		new ImageIcon(adultIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));

	adultImg.setSize(200, 200);
	adultImg.setLocation(400, 240);
	add(babyImg);

	babyImg.setSize(200, 200);
	babyImg.setLocation(240, 150);
	add(adultImg);

	// 추가 스테이지로 넘어갈 수 있는 버튼
	JButton extraStageButton = new JButton("EXTRA STAGE");
	extraStageButton.setLocation(110, 410);
	extraStageButton.setFont(new Font("DungGeunMo", Font.PLAIN, 20));
	extraStageButton.setOpaque(false);
	extraStageButton.setBackground(new Color(0, 0, 0, 0));
	extraStageButton.setSize(200, 40);
	extraStageButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		// 게임 정보 재설정(현 stage+1) 후 추가 스테이지로 넘어감
		gameFrame.setGameInfo(gameFrame.getLevel(), gameFrame.getStage() + 1, gameFrame.getMonster(),
			gameFrame.getPlayerName());
		gameFrame.toGamePanel();
	    }
	});
	add(extraStageButton);

	// 게임을 재시작할 수 있는 버튼
	JButton restartButton = new JButton("RESTART");
	restartButton.setLocation(330, 410);
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

	// 나가기 버튼
	JButton exitButton = new JButton("EXIT");
	exitButton.setLocation(550, 410);
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

    class BarLabel extends JLabel {
	int barSize = 0;

	// 바 채우기 메소드
	synchronized public void fill() {
	    barSize += 30;
	    repaint();
	    notify();
	}

	// 바 비우기 메소드
	synchronized public void consume() {
	    // 바 크기가 0이면 스레드를 wait 상태로 바꿈
	    if (barSize == 0) {
		try {
		    wait();
		} catch (InterruptedException e) {
		}
	    }
	    barSize -= 10;
	    repaint();
	    notify();
	}

	@Override // 현재 사이즈에 맞게 바 그리기
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.setColor(Color.MAGENTA);
	    int width = (int) (((double) (this.getWidth())) / maxBarSize * barSize);
	    if (width == 0)
		return;
	    g.fillRect(0, 0, width, 70);
	}

	// 바의 현재 사이즈를 리턴하는 메소드
	public int getBarSize() {
	    return barSize;
	}
    }

    class BarThread extends Thread {
	private BarLabel bar;

	public BarThread(BarLabel bar) {
	    this.bar = bar;
	}

	// 바 스레드를 wait 상태로 바꾸는 메소드
	synchronized public void stopBar() {
	    try {
		wait();
	    } catch (InterruptedException e) {
	    }
	}

	@Override
	public void run() {
	    while (true) {
		try {
		    // 현재 바의 크기가 최대 바 사이즈와 같거나 클 경우 
		    // 스레드를 멈추고 다음 스테이지로 넘어가는 버튼을 보이게 함
		    if (bar.getBarSize() >= maxBarSize) {
			loadAudio("audio/stageClear.wav");
			nextButton.setVisible(true);
			revalidate();
			repaint();
			stopBar();
			this.interrupt();
			return;
		    }
		    // 0.1초 간격으로 바 크기를 줄임
		    sleep(100);
		    bar.consume();
		} catch (InterruptedException e) {
		    return;
		}
	    }
	}
    }

    // 사운드 재생 메소드
    private void loadAudio(String fileName) {
	try {
	    clip = AudioSystem.getClip();
	    File audioFile = new File(fileName);
	    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
	    clip.open(audioStream);
	    clip.start();
	} catch (LineUnavailableException e) {
	    e.printStackTrace();
	} catch (UnsupportedAudioFileException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	ImageIcon background = new ImageIcon("image/background/startBackground.jpg");
	Image backgroundImg = background.getImage();
	g.drawImage(backgroundImg, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}
