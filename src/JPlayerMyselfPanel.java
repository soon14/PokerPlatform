import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JPlayerMyselfPanel extends JPanel {
	public ArrayList<JLabel> preCards  = new ArrayList<JLabel>();
	public JPanel panelPreCard = new JPanel();
	public JPanel cardPanel = new JPanel();
	public JLabel usernameLabel = new JLabel("用户名");
	public JLabel chipLabel = new JLabel("筹码");
	public JPanel usernameAndChip = new JPanel();
	public JPanel leftPanel = new JPanel();
	public JPanel middlePanel = new JPanel();
	public JPanel rightPanel = new JPanel();
	

	public JPlayerMyselfPanel() {
		// TODO Auto-generated constructor stub

	    
		JPanel cardPanel = new JPanel();
		Dimension cardPanelDim = new Dimension(140, 40);
		cardPanel.setSize(cardPanelDim);
		cardPanel.setMaximumSize(cardPanelDim);
		cardPanel.setMinimumSize(cardPanelDim);
		cardPanel.setPreferredSize(cardPanelDim);
		panelPreCard.setLayout(new FlowLayout());
		cardPanel.add(panelPreCard);
		
		
		Dimension usernameLabelDim = new Dimension(70, 30);
		usernameLabel.setSize(usernameLabelDim);
		usernameLabel.setMaximumSize(usernameLabelDim);
		usernameLabel.setMinimumSize(usernameLabelDim);
		usernameLabel.setPreferredSize(usernameLabelDim);
		//JLabel chipLabel = new JLabel("筹码");
		chipLabel.setSize(usernameLabelDim);
		
		//JPanel usernameAndChip = new JPanel();
		usernameAndChip.setLayout(new FlowLayout());
		usernameAndChip.add(usernameLabel);
		usernameAndChip.add(chipLabel);
		
		
		//JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.add(cardPanel);
		leftPanel.add(usernameAndChip);
		
		//JPanel middlePanel = new JPanel();
		middlePanel.add(new JLabel("扑克操作区"));
		Dimension middlePanelDim = new Dimension(500, 70);
		middlePanel.setSize(middlePanelDim);
		middlePanel.setMinimumSize(middlePanelDim);
		middlePanel.setMaximumSize(middlePanelDim);
		middlePanel.setPreferredSize(middlePanelDim);
		
		
		//JPanel rightPanel = new JPanel();
		rightPanel.add(new JLabel("聊天输入区"));
		Dimension rightPanelDim = new Dimension(140, 70);
		rightPanel.setSize(rightPanelDim);
		rightPanel.setMaximumSize(rightPanelDim);
		rightPanel.setMinimumSize(rightPanelDim);
		rightPanel.setPreferredSize(rightPanelDim);
		
		
		this.setLayout(new FlowLayout());
		
		this.add(leftPanel);
		this.add(middlePanel);
		this.add(rightPanel);
		
	}

	public JPlayerMyselfPanel(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public JPlayerMyselfPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public JPlayerMyselfPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}
	
	public void addPrevateCard(Card card) {
//		ImageIcon card1 =;
//		ImageIcon card2 =;
//		card1.setImage(card1.getImage().getScaledInstance(45,50 ,Image.SCALE_DEFAULT ));
//		card2.setImage(card2.getImage().getScaledInstance(45,50 ,Image.SCALE_DEFAULT ));
//		JLabel cradLabel1 = new JLabel();
//		JLabel cradLabel2 = new JLabel();
//		cradLabel1.setIcon(card1);
//		cradLabel2.setIcon(card2);
//		preCards.add(cradLabel1);
//		preCards.add(cradLabel2);
//		for(int i = 0; i< preCards.size(); i++) {
//			panelPreCard.add(preCards.get(i));
//		}
		
	}

}
