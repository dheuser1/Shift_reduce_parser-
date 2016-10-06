import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Scanner;

public class driver 
{
	private static Stack<String> symbols = new Stack<String>();
	private static Stack<String> operators = new Stack<String>();
	private static Stack<Integer> nums = new Stack<Integer>();
	private static ArrayList<String> input = new ArrayList<String>();
	private static int current_state=0;
	//to keep track of how much of the input has been read
	private static int i=0;
	//to tell if the string was accepted or not
	private static boolean done=false;

	//as soon as an operator is found this will do the calculation and then push the number to the stack
	public static void calculate()
	{		
		//only want to do it if there are at least 2 numbers and an operator 
		if(!operators.empty() && nums.size()>=2)
		{
			String op=operators.pop();
			int temp_1=nums.pop();
			int temp_2=nums.pop();
			
			if(op.equals("+"))
			{
				nums.push(temp_1+temp_2);
			}
			else
			{
				nums.push(temp_1*temp_2);
			}
		}
	}
	
	//this is called when a + is popped from the stack
	public static void num_add()
	{
		operators.push("+");
		calculate();
	}
	
	//this is called when a * is popped from the stack
	public static void num_multiply()
	{
		operators.push("*");
		calculate();
	}
	
	//will only read in token if it is a number
	public static boolean is_num(String str)
	{
		for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c))
	        {
	        	return false;
	        }
	    }
	    return true;
	}
	
	//sees if the input string has what it should and if it does reads it in
	public static void find(String s)
	{
		if(s.equals(input.get(i)))
		{
			i++;
		}
		else
		{
			System.out.println("error in find");
			System.exit(1);
		}
	}
	//does E->T
	public static void E_T()
	{
		//will go up to T but not pop it
		//will need to look at what is popped and change num stack accordingly
		while(symbols.peek().charAt(0)!='T')
		{
			if(symbols.size()==1)
			{
				System.out.println("error E_T 1");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		//will push in an E but need to see what state it will have by looking at top state post pop
		//if the state is 0 and not 10
		if(symbols.peek().charAt(symbols.peek().length()-1)=='0'
		   &&symbols.peek().charAt(symbols.peek().length()-2)!='1')
		{
			symbols.push("E:1");
			current_state=1;
		}
		else if(symbols.peek().charAt(symbols.peek().length()-1)=='4')
		{
			symbols.push("E:8");
			current_state=8;
		}
		else
		{
			System.out.println("error E_T 2");
			System.exit(1);
		}
	}
	
	//does T->F
	public static void T_F()
	{
		//will go up to E but not pop it
		//will need to look at what is popped and change num stack accordingly
		while(symbols.peek().charAt(0)!='F')
		{
			if(symbols.size()==1)
			{
				System.out.println("error T_F 1");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		//will push in an E but need to see what state it will have by looking at top state post pop
		if(symbols.peek().charAt(symbols.peek().length()-1)=='0'
		   &&symbols.peek().charAt(symbols.peek().length()-2)!='1')
		{
			symbols.push("T:2");
			current_state=2;
		}
		else if(symbols.peek().charAt(symbols.peek().length()-1)=='4')
		{
			symbols.push("T:2");
			current_state=2;
		}
		else if(symbols.peek().charAt(symbols.peek().length()-1)=='6')
		{
			symbols.push("T:9");
			current_state=9;
		}
		else
		{
			System.out.println("error T_F 2");
			System.exit(1);
		}
	}
	//does F->int
	public static void F_n()
	{
		//will need to look at what is popped and change num stack accordingly
		String temp=symbols.peek();
		//get rid of : and the state, only want the number
		if(temp.charAt(2)==':')
		{
			temp=temp.substring(0, temp.length()-2);
		}
		else
		{
			temp=temp.substring(0, temp.length()-3);
		}
		while(!(is_num(temp)))
		{
			if(symbols.size()==1)
			{
				System.out.println("error F_n 1");
				System.exit(1);
			}

			temp=symbols.pop();
			if(temp.charAt(2)==':')
			{
				temp=temp.substring(0, temp.length()-2);
			}
			else
			{
				temp=temp.substring(0, temp.length()-3);
			}
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		//will push in an E but need to see what state it will have by looking at top state post pop
		
		//turn n into F
		char top_state=symbols.peek().charAt(symbols.peek().length()-1);
		if((top_state=='0' || top_state=='4' || top_state=='6') && symbols.peek().charAt(symbols.peek().length()-2)!='1')
		{
			symbols.push("F:3");
			current_state=3;
		}
		else if(top_state=='7')
		{
			symbols.push("F:10");
			current_state=10;
		}
		else
		{
			System.out.println("error F_n 2");
			System.exit(1);
		}
	}
	
	//does E->E+T
	public static void E_E_PLUS_T()
	{
		while(symbols.peek().charAt(0)!='T')
		{
			if(symbols.size()==1)
			{
				System.out.println("error E_E_PLUS_T 1");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		while(symbols.peek().charAt(0)!='+')
		{
			if(symbols.size()==1)
			{
				System.out.println("error E_E_PLUS_T 2");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		//just popped + so need to add
		num_add();
		while(symbols.peek().charAt(0)!='E')
		{
			if(symbols.size()==1)
			{
				System.out.println("error E_E_PLUS_T 3");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		//ready to push in E
		if(symbols.peek().charAt(symbols.peek().length()-1)=='0'
		   &&symbols.peek().charAt(symbols.peek().length()-2)!='1')
		{
			symbols.push("E:1");
			current_state=1;
		}
		else if(symbols.peek().charAt(symbols.peek().length()-1)=='4')
		{
			symbols.push("E:8");
			current_state=8;
		}
		else
		{
			System.out.println("error E_E_PLUS_T 4");
			System.exit(1);
		}
	}
	
	//does T->T*F
	public static void T_T_TIME_F()
	{
		while(symbols.peek().charAt(0)!='F')
		{
			if(symbols.size()==1)
			{
				System.out.println("error T_T_TIME_F 1");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		while(symbols.peek().charAt(0)!='*')
		{
			if(symbols.size()==1)
			{
				System.out.println("error T_T_TIME_F 2");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		//just popped * so need to add
		num_multiply();
		while(symbols.peek().charAt(0)!='T')
		{
			if(symbols.size()==1)
			{
				System.out.println("error T_T_TIME_F 3");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		//ready to push in T
		if(symbols.peek().charAt(symbols.peek().length()-1)=='0'
		   &&symbols.peek().charAt(symbols.peek().length()-2)!='1')
		{
			symbols.push("T:2");
			current_state=2;
		}
		else if(symbols.peek().charAt(symbols.peek().length()-1)=='4')
		{
			symbols.push("T:2");
			current_state=2;
		}
		else if(symbols.peek().charAt(symbols.peek().length()-1)=='6')
		{
			symbols.push("T:9");
			current_state=9;
		}
		else
		{
			System.out.println("error  T_T_TIME_F 4");
			System.exit(1);
		}
	}
	
	//does F->(E)
	public static void F_OPEN_E_CLOSE()
	{
		while(symbols.peek().charAt(0)!=')')
		{
			if(symbols.size()==1)
			{
				System.out.println("error F_OPEN_E_CLOSE 1");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		while(symbols.peek().charAt(0)!='E')
		{
			if(symbols.size()==1)
			{
				System.out.println("error F_OPEN_E_CLOSE 2");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		while(symbols.peek().charAt(0)!='(')
		{
			if(symbols.size()==1)
			{
				System.out.println("error F_OPEN_E_CLOSE 3");
				System.exit(1);
			}
			
			String temp=symbols.pop();
			
			if(temp.charAt(0)=='+')
			{
				num_add();
			}
			else if(temp.charAt(0)=='*')
			{
				num_multiply();
			}
		}
		symbols.pop();
		//ready to push in F
		if(symbols.peek().charAt(symbols.peek().length()-1)=='0'
		   &&symbols.peek().charAt(symbols.peek().length()-2)!='1')
		{
			symbols.push("F:3");
			current_state=3;
		}
		else if(symbols.peek().charAt(symbols.peek().length()-1)=='4')
		{
			symbols.push("F:3");
			current_state=3;
		}
		else if(symbols.peek().charAt(symbols.peek().length()-1)=='6')
		{
			symbols.push("F:3");
			current_state=3;
		}
		else if(symbols.peek().charAt(symbols.peek().length()-1)=='7')
		{
			symbols.push("F:10");
			current_state=10;
		}
		else
		{
			System.out.println("error F_OPEN_E_CLOSE 4");
			System.exit(1);
		}
	}
	
	//found int in input string see what to do next
	public static void n()
	{
		if(current_state==0 || current_state==4 || current_state==6 || current_state==7)
		{
			if(is_num(input.get(i)))
			{
				nums.push(Integer.parseInt(input.get(i)));
				symbols.push("n:5");
				current_state=5;
				i++;
			}
			else
			{
				System.out.println("error n 1");
				System.exit(1);
			}
		}
	}
	
	//found + in input string see what to do next
	public static void plus()
	{
		if(current_state==1 || current_state==8)
		{
			find("+");
			symbols.push(input.get(i-1)+":6");
			current_state=6;
		}
		else if(current_state==2)
		{
			E_T();
		}
		else if(current_state==3)
		{
			T_F();
		}
		else if(current_state==5)
		{
			F_n();
		}
		else if(current_state==9)
		{
			E_E_PLUS_T();
		}
		else if(current_state==10)
		{
			T_T_TIME_F();
		}
		else if(current_state==11)
		{
			F_OPEN_E_CLOSE();
		}
		else
		{
			System.out.println("error plus 1");
			System.exit(1);
		}
	}
	
	//found * in input string see what to do next
	public static void time()
	{
		if(current_state==2 || current_state==9)
		{
			find("*");
			symbols.push(input.get(i-1)+":7");
			current_state=7;
		}
		else if(current_state==3)
		{
			T_F();
		}
		else if(current_state==5)
		{
			F_n();
		}
		else if(current_state==10)
		{
			T_T_TIME_F();
		}
		else if(current_state==11)
		{
			F_OPEN_E_CLOSE();
		}
		else
		{
			System.out.println("error time 1");
			System.exit(1);
		}
	}
	//found ( in input string see what to do next
	public static void open()
	{
		if(current_state==0 || current_state==4 || current_state==6 || current_state==7)
		{
			find("(");
			symbols.push(input.get(i-1)+":4");
			current_state=4;
		}
		else
		{
			System.out.println("error open 1");
			System.exit(1);
		}
	}
	
	//found ) in input string see what to do next
	public static void close()
	{
		if(current_state==2)
		{
			E_T();
		}
		else if(current_state==3)
		{
			T_F();
		}
		else if(current_state==5)
		{
			F_n();
		}
		else if(current_state==8)
		{
			find(")");
			symbols.push(input.get(i-1)+":11");
			current_state=11;
		}
		else if(current_state==9)
		{
			E_E_PLUS_T();
		}
		else if(current_state==10)
		{
			T_T_TIME_F();
		}
		else if(current_state==11)
		{
			F_OPEN_E_CLOSE();
		}
		else
		{
			System.out.println("error close 1");
			System.exit(1);
		}
		
	}
	
	//found $ in input string see what to do next
	public static void money()
	{
		if(current_state==1)
		{
			System.out.println("accepted");
			done=true;
		}
		else if(current_state==2)
		{
			E_T();
		}
		else if(current_state==3)
		{
			T_F();
		}
		else if(current_state==5)
		{
			F_n();
		}
		else if(current_state==9)
		{
			E_E_PLUS_T();
		}
		else if(current_state==10)
		{
			T_T_TIME_F();
		}
		else if(current_state==11)
		{
			F_OPEN_E_CLOSE();
		}
		else
		{
			System.out.println("error money 1");
			System.exit(1);
		}
	}

	
	public static void main(String[] args) 
	{
		symbols.push("-:0");
		
		String test=args[0];
		
		StringTokenizer st = new StringTokenizer(test, "(+)*", true);
		    
		while (st.hasMoreTokens()) 
	    {
	        input.add(st.nextToken());	
	    }
		//add in the $ so user does not have to
		input.add("$");
		
		System.out.print(symbols.toString()+"   ");
		for(int j=i; j<input.size(); j++)
		{
			System.out.print(input.get(j)+" ");
		}
		
		System.out.println("");
		
		while(i<input.size() && done==false)
		{
			if(input.get(i).charAt(0)=='+')
			{
				plus();
				System.out.print(symbols.toString()+"   ");
				for(int j=i; j<input.size(); j++)
				{
					System.out.print(input.get(j)+" ");
				}
				
				System.out.println("");
			}
			else if(input.get(i).charAt(0)=='*')
			{
				time();
				System.out.print(symbols.toString()+"   ");
				for(int j=i; j<input.size(); j++)
				{
					System.out.print(input.get(j)+" ");
				}
				System.out.println("");
			}
			else if(input.get(i).charAt(0)=='(')
			{
				open();
				System.out.print(symbols.toString()+"   ");
				for(int j=i; j<input.size(); j++)
				{
					System.out.print(input.get(j)+" ");
				}
				System.out.println("");
			}
			else if(input.get(i).charAt(0)==')')
			{
				close();
				System.out.print(symbols.toString()+"   ");
				for(int j=i; j<input.size(); j++)
				{
					System.out.print(input.get(j)+" ");
				}
				System.out.println("");
			}
			else if(input.get(i).charAt(0)=='$')
			{
				money();
				System.out.print(symbols.toString()+"   ");
				for(int j=i; j<input.size(); j++)
				{
					System.out.print(input.get(j)+" ");
				}
				System.out.println("");
			}
			//if it is not +*()$ it must be a number
			else
			{
				n();
				System.out.print(symbols.toString()+"   ");
				for(int j=i; j<input.size(); j++)
				{
					System.out.print(input.get(j)+" ");
				}
				System.out.println("");
			}
		}
		//it was accepted so print its value
		if(done==true)
		{
			calculate(); 
			System.out.println("the value is "+nums.peek());
		}
		//was not accepted
		else
		{
			System.out.println("not accepted"); 
		}
	}
}
