

import java.util.ArrayList;

/*
�����˿�AI��������
����������Һ���Ϸ����
����Ϸ�������߸ÿ����������ı仯�����ֵ��ÿ��������ÿ����������������

*/
public class TexasAIController{
    private Player player; //���
    private int difficulty;  //�Ѷ� 1 ��   2 �е�    3��
	private TexasGameHelper helper;
    public TexasAIController(Player player, int dif) {
        this.player = player;
        this.difficulty = dif;
    }



    /*
    �¼����
    ������AI�������࣬����ֻ�ᱻ�Լ���update()��������
    ���ÿ�����������update()���������ǻᾭ��һϵ�еļ��㣬�ó�һ������
    ���������еķ����ʹ���ѡ����ĳһ����
    �����ƽ̨�߼�ʵ�֣�AI����ֻ�����
    */
    //��ע
    public void handler_add_bet(int addTo) { 
		helper = player.texasGameHelper;
        helper.action_add_bet(player, addTo);
    }

    //��ע
    public void handler_follow_bet(int followTo){  
		helper = player.texasGameHelper;
        helper.action_follow_bet(player, followTo);
    }
    //����/����
    public void handler_abort_bet() {  
		helper = player.texasGameHelper;
        helper.action_abort_bet(player);
    }
    //����
    public void handler_check() {  
		helper = player.texasGameHelper;
        helper.action_check(player);
    }
    
    //׼����
    public void handler_ready() {
		helper = player.texasGameHelper;
        helper.action_ready(player);
    }


    //������Ϸ��������ã�msg�лᴫ������Ҫ��������Ϣ
    //����ֵ����AI���в�������ô��
    //Ȼ��update�����������һ�����ԣ���ѡ����������һ�������
    //����update���������κ���
    public void update(TexasMessage msg) {
		if(msg.whatHappen == 3) {
			handler_ready();
			return;
		}
		if(!msg.isYourTurn)return;
        switch(this.difficulty) { //���ݲ�ͬ�Ѷ�ѡ��ͬ����
            case 1: //��
                strategy_easy(msg);
            case 2: //�е�
                strategy_medium(msg);
            case 3: //����
                strategy_hard(msg);
        }
    }

    public void strategy_easy(TexasMessage msg) {
		
		System.out.println("into strategy, round" + msg.round);
        int mybet=msg.playerMoney[msg.playerIndex];
		int minbet=getminbet(msg);
		int followTo = msg.followTo;
    	if(msg.round != 1) 
        {
            ArrayList<Card> currentcard=(ArrayList<Card>)msg.private_cards.clone();
            currentcard.addAll(msg.public_cards);
            if(is_straightflush(currentcard)&&msg.add)
            {
          	  if(mybet>minbet)
          	  {
          		  handler_add_bet((int)(minbet+ (mybet-minbet)/3));
          	  }
          	  else 
          	  {
          		  //System.out.println("All in");
          		  handler_follow_bet(followTo);
          	  }
            }
            else if(is_fourofakind(currentcard)&&msg.add)
            {
          	  if(mybet>minbet)
          	  {
          		  handler_add_bet((int)(minbet+(mybet-minbet)/4));
          	  }
          	  else // if(msg.addmin==msg.addmax)
          	  {
          		  //System.out.println("All in"); ////
          		 handler_follow_bet(followTo);
          	  }
            }
            else if(is_fullhouse(currentcard)&&msg.add)
            {
          	  if(mybet>minbet)
          	  {
          		  handler_add_bet((int)(minbet+(mybet-minbet)/5));
          	  }
          	  else
          	  {
          		  //System.out.println("All in");////
          		 handler_follow_bet(followTo);
          	  }
            }
            else if(is_flush(currentcard)&&(msg.add||msg.check||msg.follow))
            {
          	  if(mybet>minbet)
          	  {
          		  if(msg.add)
          		  {
          			  double seed=Math.random();
          			  if(seed>0.7)
          				  handler_add_bet((minbet+(mybet-minbet)/10));
          		  }
          		  else if(msg.check||msg.follow){
					if(msg.check) {
						handler_check();
					} else {
						handler_follow_bet(followTo);
					}
				  }
          	  }
          	  else
          	  {
          		  double seed=Math.random();
          		  if(seed>0.5)
          		  {
          			  handler_abort_bet();
          		  }
          		  else 
          		  {
          			  //System.out.println("All in"); ////
					if(msg.check) {
						handler_check();
					} else {
						handler_follow_bet(followTo);
					}
          		  }
          	  }
            }
            else if(is_straight(currentcard)&&(msg.add||msg.check||msg.follow))
            {
          	  	if(mybet>minbet)
          	  	{
					double seed=Math.random();
					if(seed>0.8)
          		  		handler_add_bet((minbet+(mybet-minbet)/10));
          		  	else {
						if(msg.check) {
							handler_check();
						} else {
							handler_follow_bet(followTo);
						}
						
				 	}
          			  
          	  	}
          		else
          	  	{
          		  double seed=Math.random();
          		  if(seed>0.4)
          		  {
          			  handler_abort_bet();
          		  }
          		  else 
          		  {
          			  //System.out.println("All in");/////
					if(msg.check) {
						handler_check();
					} else {
						handler_follow_bet(followTo);
					}
          		  }
          	  	}
            }
            else if(haspair(currentcard)==0)// too small , abort
            {
          	  handler_abort_bet();
            }
            else
            {
          	  if(msg.add&&mybet>minbet)
          	  {
				if(msg.check) {
					handler_check();
				} else {
					handler_follow_bet(followTo);
				}
          	  }
          	  else
          	  {
          		  handler_abort_bet();
          	  }
            }
        }
        else if(msg.round==1) //in first round
        {
           ArrayList<Card> currentcard=msg.private_cards;
           Card card1=currentcard.get(0);
           Card card2=currentcard.get(1);
           if((card1.face==card2.face)&&card1.face>=10)
           {
             handler_add_bet(Math.min(mybet, minbet* 11/10));
           }
           else if(card1.face<6&&card2.face<6&&card1.face!=card2.face&&card1.suit!=card2.suit)//if priavtecards are too small, then abort
           {
             handler_abort_bet();
           }
           else
           {
			if(msg.check) {
				handler_check();
			} else {
				handler_follow_bet(followTo);
			}
           }
		}
		else {
			handler_abort_bet();
		}
		
    }

    public void strategy_medium(TexasMessage msg) {

    }

    public void strategy_hard(TexasMessage msg) {

    }

    public void update(RoomMessage msg) {
        //do nothing
        return;
    }
    
    private ArrayList<Card> sortbyface(ArrayList<Card> pokers)
    {
    	Card tempInt;
    	for (int i = 1; i < pokers.size(); i++) {
    		for (int j = i; j > 0 && (pokers.get(j).face < pokers.get(j - 1).face); j--) {
    			tempInt = pokers.remove(j);
    			pokers.add(j - 1, tempInt);
    		}
    	}
    		return pokers;
    }
    
    private ArrayList<Card> sortbysuit(ArrayList<Card> pokers)
    {
    	Card tempInt;
    	for (int i = 1; i < pokers.size(); i++) {
    		for (int j = i; j > 0 && (pokers.get(j).suit < pokers.get(j - 1).suit); j--) {
    			tempInt = pokers.remove(j);
    			pokers.add(j - 1, tempInt);
    		}
    	}
    		return pokers;
    }

    
    public boolean is_straightflush(ArrayList<Card> pokers)
    {
    	boolean is=false;
    	if(pokers.size()==5)
    	{
    		is=is_straight(pokers)&&is_flush(pokers);
    	}
    	else if(pokers.size()==6)
    	{
    		Card tempcard;
    		for(int i=0;i<6;i++)
    		{
    			tempcard = pokers.remove(i);
    			if(is_straight(pokers)&&is_flush(pokers))
    			{
    				is=true;
    				break;
    			}
    			pokers.add(i, tempcard);
    		}
    	}
    	else if(pokers.size()==7)
    	{
    		Card tempcard1;
    		Card tempcard2;
    		for(int i=0;i<7;i++)
    		{
    			tempcard1=pokers.remove(i);
    			for(int j=0;j<6;j++)
    			{
    				tempcard2=pokers.remove(j);
        			if(is_straight(pokers)&&is_flush(pokers))
        			{
        				is=true;
        				return is;
        			}
        			pokers.add(j, tempcard2);
    			}
    			pokers.add(i,tempcard1);
    		}
    	}
    	return is;
    }
    
    public boolean is_fourofakind(ArrayList<Card> pokers)
    {
    	pokers=sortbyface(pokers);
    	boolean is=false;
    	if(pokers.size()==5) // first round, only got 5 cards
    	{
    		for(int i=0;i<2;i++)
    		{
    			if(pokers.get(i).face==pokers.get(i+1).face&&pokers.get(i+1).face==pokers.get(i+2).face&&pokers.get(i+2).face==pokers.get(i+3).face)
    				{
    					is=true;
    					break;
    				}
    		}
    	}
    	else if(pokers.size()==6) // second round got 6 cards
    	{
    		for(int i=0;i<3;i++)
    		{
    			if(pokers.get(i).face==pokers.get(i+1).face&&pokers.get(i+1).face==pokers.get(i+2).face&&pokers.get(i+2).face==pokers.get(i+3).face)
    				{
    					is=true; 
    					break;
    				}
    		}
    	}
    	else if(pokers.size()==7) // second round got 7 cards
    	{
    		for(int i=0;i<4;i++)
    		{
    			if(pokers.get(i).face==pokers.get(i+1).face&&pokers.get(i+1).face==pokers.get(i+2).face&&pokers.get(i+2).face==pokers.get(i+3).face)
    				{
    					is=true;
    					break;
    				}
    		}
    	}
		return is;
    }
    
    public boolean is_fullhouse(ArrayList<Card> pokers)
    {	
    	boolean is=false;
    	ArrayList<Card> sortedpokers=sortbyface(pokers);
    	if(pokers.size()==5) // first round, only got 5 cards
    	{
    		if(sortedpokers.get(0).face==sortedpokers.get(1).face&&sortedpokers.get(1).face==sortedpokers.get(2).face&&sortedpokers.get(3).face==sortedpokers.get(4).face)
    			is=true;
    		else if(sortedpokers.get(0).face==sortedpokers.get(1).face&&sortedpokers.get(2).face==sortedpokers.get(3).face&&sortedpokers.get(3).face==sortedpokers.get(4).face)
    			is=true;
    	}
    	else if(pokers.size()==6) // second round got 6 cards
    	{
    		for(int i=0;i<6;i++)
    		{
    			Card tempcard=sortedpokers.remove(i);
        		if(sortedpokers.get(0).face==sortedpokers.get(1).face&&sortedpokers.get(1).face==sortedpokers.get(2).face&&sortedpokers.get(3).face==sortedpokers.get(4).face)
        			is=true;
        		else if(sortedpokers.get(0).face==sortedpokers.get(1).face&&sortedpokers.get(2).face==sortedpokers.get(3).face&&sortedpokers.get(3).face==sortedpokers.get(4).face)
        			is=true;
        		sortedpokers.add(i, tempcard);
    		}
    	}
    	else if(pokers.size()==7) // second round got 7 cards
    	{
    		for(int i=0;i<7;i++)
    		{
    			Card tempcard1=sortedpokers.remove(i);
    			for(int j=0;j<6;j++)
    			{
    				Card tempcard2=sortedpokers.remove(j);
            		if(sortedpokers.get(0).face==sortedpokers.get(1).face&&sortedpokers.get(1).face==sortedpokers.get(2).face&&sortedpokers.get(3).face==sortedpokers.get(4).face)
            			is=true;
            		else if(sortedpokers.get(0).face==sortedpokers.get(1).face&&sortedpokers.get(2).face==sortedpokers.get(3).face&&sortedpokers.get(3).face==sortedpokers.get(4).face)
            			is=true;
            		sortedpokers.add(j, tempcard2);
    			}
    			sortedpokers.add(i,tempcard1);
    		}
    	}
    	return is;
    }
    
    public boolean is_flush(ArrayList<Card> pokers)
    {
    	boolean is=false;
    	if(pokers.size()==5)
    	{
        		if(pokers.get(0).suit==pokers.get(1).suit&&pokers.get(1).suit==pokers.get(2).suit&&pokers.get(2).suit==pokers.get(3).suit&&pokers.get(3).suit==pokers.get(4).suit)
        			is=true;
    	}
    	else if(pokers.size()==6)
    	{
    		ArrayList<Card> temp=sortbysuit(pokers);
    		for(int i=0;i<2;i++)
    		{
    			if(temp.get(i).suit==temp.get(i+1).suit&&temp.get(i+1).suit==temp.get(i+2).suit&&temp.get(i+2).suit==temp.get(i+3).suit&&temp.get(i+3).suit==temp.get(i+4).suit)
    			{
    				is=true;
    			}
    		}
    	}
    	else if(pokers.size()==7)
    	{
    		ArrayList<Card> temp=sortbysuit(pokers);
    		for(int i=0;i<3;i++)
    		{
    			if(temp.get(i).suit==temp.get(i+1).suit&&temp.get(i+1).suit==temp.get(i+2).suit&&temp.get(i+2).suit==temp.get(i+3).suit&&temp.get(i+3).suit==temp.get(i+4).suit)
    			{
    				is=true;
    			}
    		}
    	}
    	return is;
    }
    
    public boolean is_straight(ArrayList<Card> pokers)
    {
    	boolean is=false;
		ArrayList<Card> sortedpokers=sortbyface(pokers);
    	if(pokers.size()==5) // first round, only got 5 cards
    	{
    		if(sortedpokers.get(0).face!=sortedpokers.get(1).face&&
    				sortedpokers.get(1).face!=sortedpokers.get(2).face&&
    				sortedpokers.get(2).face!=sortedpokers.get(3).face&&
    				sortedpokers.get(3).face!=sortedpokers.get(4).face&&
    				(sortedpokers.get(4).face-sortedpokers.get(0).face)==4)
    		{
    			is=true;
    		}
    	}
    	else if(pokers.size()==6) // second round got 6 cards
    	{
    		for(int i=0;i<6;i++)
    		{
    			ArrayList<Card> temp=(ArrayList<Card>) sortedpokers.clone();
    			temp.remove(i);
        		if(temp.get(0).face!=temp.get(1).face&&
        				temp.get(1).face!=temp.get(2).face&&
        				temp.get(2).face!=temp.get(3).face&&
        				temp.get(3).face!=temp.get(4).face&&
        				(temp.get(4).face-temp.get(0).face)==4)
        		{
        			is=true;
        			break;
        		}
    		}
    	}
    	else if(pokers.size()==7) // second round got 7 cards
    	{
    		for(int i=0;i<7;i++)
    		{
    			ArrayList<Card> temp=(ArrayList<Card>) sortedpokers.clone();
    			temp.remove(i);
    			for(int j=i;j<6;j++)
    			{
    				Card temp1=temp.remove(j);
            		if(temp.get(0).face!=temp.get(1).face&&
            				temp.get(1).face!=temp.get(2).face&&
            				temp.get(2).face!=temp.get(3).face&&
            				temp.get(3).face!=temp.get(4).face&&
            				(temp.get(4).face-temp.get(0).face)==4)
            		{
            			is=true;
            			break;
    			}
            		temp.add(j, temp1);
    		}
    	}
    }
    	return is;
    }
    
    public int diff_straightflush(ArrayList<Card> pokers)
    {
    	int diff=-1;
    	ArrayList<Card> sortedpokers=sortbyface(pokers);
    	ArrayList<Card> sortedpokers_color=sortbysuit(pokers);
		for(int i=0;i<pokers.size()-3;i++)
		{
			if(sortedpokers_color.get(i).suit==sortedpokers_color.get(i+1).suit&&sortedpokers_color.get(i+1).suit==sortedpokers_color.get(i+2).suit&&
					sortedpokers_color.get(i+2).suit==sortedpokers_color.get(i+3).suit)
			{
				ArrayList<Card> temp=new ArrayList<Card>();
				temp.add(sortedpokers_color.get(i));
				temp.add(sortedpokers_color.get(i+1));
				temp.add(sortedpokers_color.get(i+2));
				temp.add(sortedpokers_color.get(i+3));
				temp=sortbyface(temp);
        		if(temp.get(0).face!=temp.get(1).face&&
        				temp.get(1).face!=temp.get(2).face&&
        				temp.get(2).face!=temp.get(3).face&&
        				((temp.get(3).face-temp.get(0).face==3)||(temp.get(3).face-temp.get(0).face==4)))
        		{
        			diff=1;
        		}
			}
			// lack two cards
    		for(int j=0;j<pokers.size()-2;j++)
    		{
    			if(sortedpokers_color.get(j).suit==sortedpokers_color.get(j+1).suit&&sortedpokers_color.get(j+1).suit==sortedpokers_color.get(j+2).suit)
    			{
    				ArrayList<Card> temp=new ArrayList<Card>();
    				temp.add(sortedpokers_color.get(j));
    				temp.add(sortedpokers_color.get(j+1));
    				temp.add(sortedpokers_color.get(j+2));
    				temp=sortbyface(temp);
            		if(temp.get(0).face!=temp.get(1).face&&
            				temp.get(1).face!=temp.get(2).face&&
            				((temp.get(2).face-temp.get(0).face>1)||(temp.get(2).face-temp.get(0).face<5))&&diff==-1)
            		{
            			diff=2;
            		}
    			}
    		}			
		}
    	return diff;
    }
    
    public int diff_fourofakind(ArrayList<Card> pokers)
    {
		// if there are 3 cards that the same face, then return 1
		// if there are 2 cards that have the same face, then return 2
    	int diff=-1;
    	ArrayList<Card> sortedpokers=sortbyface(pokers);
    	if(pokers.size()==5) // first round, only got 5 cards
    	{
    		for(int i=0;i<3;i++)
    		{
    			if(!is_fourofakind(pokers)&&sortedpokers.get(i).face==sortedpokers.get(i+1).face&&sortedpokers.get(i+1).face==sortedpokers.get(i+2).face)
    				diff=1;
    		}
    		for(int i=0;i<4;i++)
    		{
    			if(!is_fourofakind(pokers)&&sortedpokers.get(i).face==sortedpokers.get(i+1).face&&diff==-1)
    				diff=2;
    		}
    	}
    	else if(pokers.size()==6) // second round got 6 cards
    	{
    		for(int i=0; i<4;i++)
    		{
    			if(!is_fourofakind(pokers)&&sortedpokers.get(i).face==sortedpokers.get(i+1).face&&sortedpokers.get(i+1).face==sortedpokers.get(i+2).face)
    			{
    				diff=1;
    			}
    		}
    		for(int i=0;i<5;i++)
    		{
    			if(!is_fourofakind(pokers)&&sortedpokers.get(i).face==sortedpokers.get(i+1).face&&diff==-1)
    				diff=2;
    		}
    	}
    	else if(pokers.size()==7) // second round got 7 cards
    	{
    		for(int i=0; i<5;i++)
    		{
    			if(!is_fourofakind(pokers)&&sortedpokers.get(i).face==sortedpokers.get(i+1).face&&sortedpokers.get(i+1).face==sortedpokers.get(i+2).face)
    			{
    				diff=1;
    			}
    		}
    		for(int i=0;i<6;i++)
    		{
    			if(!is_fourofakind(pokers)&&sortedpokers.get(i).face==sortedpokers.get(i+1).face&&diff==-1)
    				diff=2;
    		}
    	}
    	return diff;
    }
    
    
    
    public int diff_flush(ArrayList<Card> pokers)
    {
    	int diff=-1;
    	ArrayList<Card> sortedpokers=sortbysuit(pokers);
    	if(pokers.size()==5) // first round, only got 5 cards
    	{
    		// only need to consider suit
    		for(int i=0;i<2;i++)
    		{
    			if(pokers.get(i).suit==pokers.get(i+1).suit&&pokers.get(i+1).suit==pokers.get(i+2).suit&&
    					pokers.get(i+2).suit==pokers.get(i+3).suit&&(!is_flush(pokers))) 
    			{
    				diff=1;
    				break;
    			}
    		}
    		for(int i=0;i<3;i++)
    		{
    			if((!is_flush(pokers))&&pokers.get(i).suit==pokers.get(i+1).suit&&pokers.get(i+1).suit==pokers.get(i+2).suit)
    			{
    				if(diff==-1)
    				{
    					diff=2;
    					break;
    				}
    			}
    		}
    	}
    	else if(pokers.size()==6) // second round got 6 cards
    	{
    		for(int i=0;i<3;i++)
    		{
    			if(pokers.get(i).suit==pokers.get(i+1).suit&&pokers.get(i+1).suit==pokers.get(i+2).suit&&
    					pokers.get(i+2).suit==pokers.get(i+3).suit&&(!is_flush(pokers))) 
    			{
    				diff=1;
    				break;
    			}
    		}
    		for(int i=0;i<4;i++)
    		{
    			if((!is_flush(pokers))&&pokers.get(i).suit==pokers.get(i+1).suit&&pokers.get(i+1).suit==pokers.get(i+2).suit)
    			{
    				if(diff==-1)
    				{
    					diff=2;
    					break;
    				}
    			}
    		}
    	}
    	else if(pokers.size()==7) // second round got 7 cards
    	{
    		for(int i=0;i<4;i++)
    		{
    			if(pokers.get(i).suit==pokers.get(i+1).suit&&pokers.get(i+1).suit==pokers.get(i+2).suit&&
    					pokers.get(i+2).suit==pokers.get(i+3).suit&&(!is_flush(pokers))) 
    			{
    				diff=1;
    				break;
    			}
    		}
    		for(int i=0;i<5;i++)
    		{
    			if((!is_flush(pokers))&&pokers.get(i).suit==pokers.get(i+1).suit&&pokers.get(i+1).suit==pokers.get(i+2).suit)
    			{
    				if(diff==-1)
    				{
    					diff=2;
    					break;
    				}
    			}
    		}
    	}
		return diff;
    }
    
    private int getminbet(TexasMessage msg)
    {
    	return msg.addMin;
    }
    
    public int haspair(ArrayList<Card> pokers)
    {
    	int num=0;
    	ArrayList<Card> sortedcards=sortbyface(pokers);
    	for(int i=0;i<sortedcards.size()-1;)
    	{
    		if(sortedcards.get(i).face==sortedcards.get(i+1).face)
    		{
    			num++;
    			i+=2;
    		}
    		else
    		{
    			i+=1;
    		}
    	}
    	return num;
    }


    
  
}