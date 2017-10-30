/**********************************************************
 * @author Derek Yee
 * date: 7/11/17
 * matrix Multiplication
 *
 ***********************************************************/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Yee_Matrix_Algorithms
{
	//@method:classicMatrixMultiplication
	//description: uses 3 for loops
	//===============================================================================================================================
	public int[][] classicMatrixMultiplication(int[][] matrix1,int[][] matrix2)  
	{
		int[][] matrix3=new int[matrix1.length][matrix1.length];        
		for(int i=0;i<matrix1.length;i++)             //deals with rows for matrix3 and 1
		{
			for(int j=0;j<matrix2[0].length;j++)      //deals with columns for matrix 3 and 2
			{
				for(int k=0;k<matrix1[0].length;k++)   //columns for matrix 1, rows for matrix 2
				{
					matrix3[i][j]+=(matrix1[i][k]*matrix2[k][j]); //
				}
			}
		}
		return matrix3;
	}
	//===============================================================================================================================
	//@method:recurrsiveMultiplication
	//description: splits arrays into 4 parts till base case reached
	//===============================================================================================================================
	public int[][] recurrsiveMultiplication(int[][] matrix1,int[][] matrix2) //reused some strauss operations
	{
	
		int size=matrix1.length;        //reference for recurrsion 
		int productResult[][]=new int[size][size];  
		if(size==1)              //when the size is easy enough to compute "base case"
		{
			productResult[0][0]=matrix1[0][0]*matrix2[0][0];
		}
		else  //because matrix is 2^(x), if its not small enough, split into 4 parts for each matrix
		{
			int half=size/2;
			int[][] partA11=new int[half][half],partA12=new int[half][half];
			int[][] partA21=new int[half][half],partA22=new int[half][half];//first matrix split into 4 parts
			int[][] partB11=new int[half][half],partB12=new int[half][half];
			int[][] partB21=new int[half][half],partB22=new int[half][half];//second matrix split into 4 parts
			
			fill(matrix1,partA11,0,0);fill(matrix1,partA12,0,half);   
			fill(matrix1,partA21,half,0);fill(matrix1,partA22,half,half);//filling quardrants for matrix1
			
			fill(matrix2,partB11,0,0);fill(matrix2,partB12,0,half);
			fill(matrix2,partB21,half,0);fill(matrix2,partB22,half,half); //filling quadrants for matrix2
			
			//formulas to do one less multiplication, improving run time
			int[][] C11=straussAdd(recurrsiveMultiplication(partA11,partB11),recurrsiveMultiplication(partA12,partB21));
			int[][] C12=straussAdd(recurrsiveMultiplication(partA11,partB12),recurrsiveMultiplication(partA12,partB22));
			int[][] C21=straussAdd(recurrsiveMultiplication(partA21,partB11),recurrsiveMultiplication(partA22,partB21));
			int[][] C22=straussAdd(recurrsiveMultiplication(partA21,partB12),recurrsiveMultiplication(partA22,partB22));
			straussMerge(C11,productResult,0,0);straussMerge(C12,productResult,0,half);//merging results to final array
			straussMerge(C21,productResult,half,0);straussMerge(C22,productResult,half,half); 
		}
			return productResult;
	}
	//===============================================================================================================================
	//@method:straussMultiplication
	//description: uses formulas to calculate the product of two matrices
	//===============================================================================================================================
	public int[][] straussMultiplication(int[][] matrix1,int[][] matrix2)
	{ 
		int size=matrix1.length;        //reference for recurrsion 
		int productResult[][]=new int[size][size];  
		if(size==1)              //when the size is easy enough to compute "base case"
		{
			productResult[0][0]=matrix1[0][0]*matrix2[0][0];
		}
		else  //because matrix is 2^(x), if its not small enough, split into 4 parts for each matrix
		{
			int half=size/2;
			int[][] partA11=new int[half][half],partA12=new int[half][half];
			int[][] partA21=new int[half][half],partA22=new int[half][half];//first matrix split into 4 parts
			int[][] partB11=new int[half][half],partB12=new int[half][half];
			int[][] partB21=new int[half][half],partB22=new int[half][half];//second matrix split into 4 parts
			fill(matrix1,partA11,0,0);fill(matrix1,partA12,0,half);   
			fill(matrix1,partA21,half,0);fill(matrix1,partA22,half,half);//filling quardrants for matrix1
			
			fill(matrix2,partB11,0,0);fill(matrix2,partB12,0,half);
			fill(matrix2,partB21,half,0);fill(matrix2,partB22,half,half); //filling quadrants for matrix2
			
			//following Strauss equations:
			
			int[][] P=straussMultiplication(straussAdd(partA11,partA22),straussAdd(partB11,partB22));
			int[][] Q=straussMultiplication(straussAdd(partA21,partA22),partB11);
			int[][] R=straussMultiplication(partA11,straussSubtract(partB12,partB22));
			int[][] S=straussMultiplication(partA22,straussSubtract(partB21,partB11));
			int[][] T=straussMultiplication(straussAdd(partA11,partA12),partB22);
			int[][] U=straussMultiplication(straussSubtract(partA21,partA11),straussAdd(partB11,partB12));
			int[][] V=straussMultiplication(straussSubtract(partA12,partA22),straussAdd(partB21,partB22));
			int[][] C11=straussAdd(straussSubtract(straussAdd(P,S),T),V);
			int[][] C12=straussAdd(R,T);
			int[][] C21=straussAdd(Q,S);
			int[][] C22=straussAdd(straussSubtract(straussAdd(P,R),Q),U);
			//merging results to final array
			straussMerge(C11,productResult,0,0);straussMerge(C12,productResult,0,half);
			straussMerge(C21,productResult,half,0);straussMerge(C22,productResult,half,half); 
		}
			return productResult;
	}
	//===============================================================================================================================
	//@method:straussMerge
	//description: gets two integer arrays, merges them back together based on position
	//===============================================================================================================================		
	private void straussMerge(int[][] matrix1,int[][] result,int part1,int part2) 
	{
		//position1 and position 2 will keep track of particular spot in split array
		for(int i=0,position1=part1;i<matrix1.length;i++,position1++)
		{
			for(int j=0,position2=part2;j<matrix1.length;j++,position2++)
			{
				result[position1][position2]=matrix1[i][j];
				
			}
			
		}
	}
	//===============================================================================================================================
	//@method: straussSubtract
	//description: Standard matrix subtraction
	//===============================================================================================================================
	private int[][] straussSubtract(int[][] matrixA,int[][]matrixB)
	{
		int[][] difference=new int[matrixA.length][matrixA.length];
			for(int i=0;i<matrixA.length;i++)
			{
				for(int j=0;j<matrixA.length;j++)
				{
					difference[i][j]=matrixA[i][j]-matrixB[i][j];
				}
			}
			return difference;
	}
	//===============================================================================================================================
	//@method: straussAdd
	//description:standard matrix addition
	//===============================================================================================================================
	private int[][] straussAdd(int[][] matrixA,int[][]matrixB)//standard matrix adding
	{
		int[][] sum=new int[matrixA.length][matrixA.length];
			for(int i=0;i<matrixA.length;i++)
			{
				for(int j=0;j<matrixA.length;j++)
				{
					sum[i][j]=matrixA[i][j]+matrixB[i][j];
				}
			}
			return sum;
	
	
	}
	//===============================================================================================================================
	//@method:fill
	//description: will populate the split arrays given specific position
	//===============================================================================================================================
	private void fill(int[][] matrix1,int[][] matrixFill,int pos1,int pos2)
		{
						//position 1 and position 2 will keep track of particular spot of split array
			for(int i=0,position1=pos1;i<matrixFill.length;i++,position1++)
			{
				for(int j=0,position2=pos2;j<matrixFill.length;j++,position2++)
				{
					matrixFill[i][j]=matrix1[position1][position2];
				}
			}
		}
	//===============================================================================================================================
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////