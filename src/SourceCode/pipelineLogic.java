////Main logic for 5 stage pipeline
package Logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;



public class pipelineLogic {
	
	private static ArrayList<String> allInstructionDataList;
	
	public static ArrayList<ArrayList<String>> listofallLists;
	public static int clockCycle = 1;
	public static int fetchClockCounter = 0;
	public static int columnCount = 0;
	private static int whiteSpaceCount = 0;
	private static ArrayList<String> instructionNameList;
	public static ArrayList<String> outputString = new ArrayList<String>();
	
	private static Map<String, String> dictionary = new HashMap();
	
	//Fill the lists of all instruction data
	public  void fillInstructionDataArray (ArrayList<String> instructionArray){
		
		listofallLists = new ArrayList<ArrayList<String>>();
		//Clear the list at the start of program
		listofallLists.clear();
		
		
		FetchInstructionData(instructionArray);
	}
	
	////Fetches the instruction, splits it into instruction name, input dependency and output dependency and feeds 
	////it to the Run Pipeline function.
	private static void FetchInstructionData(ArrayList<String> instructionArray) {
		
		String fullInstructionName = null;
		
		String instructionName = "";
		String inputDependency1 = "";
		String inputDependency2 = "";
		String outputDependency = "";
		String outputDependency2 = "";
		//Reset all counters and lists
		clockCycle = 1;
		fetchClockCounter = 0;
		columnCount = 0;
		whiteSpaceCount = 0;
		instructionNameList = new ArrayList<String>();
		instructionNameList.clear();
		outputString.clear();
		dictionary.clear();
		
		if(instructionArray.isEmpty()){
			System.out.println("The input text file is empty");
		}
		
		else{
			
				for(int index=0; index <instructionArray.size(); index++){
					
					//Clock cycle for each stage of the pipeline
					clockCycle = fetchClockCounter+1;
					
					String instruction = instructionArray.get(index);
					fullInstructionName = instruction;
					
					//Split Instruction into instruction name, input dependency and output dependency
					String[] parts = instruction.split(" ");

						instructionName = parts[0]; // instructionName
						
						//This list is used later to calculate instruction frequency
						instructionNameList.add(instructionName);
						
						if(instructionName.equalsIgnoreCase("BZ")){
							inputDependency1 = parts[1];
							outputDependency = parts[2];
						}
						else if(instructionName.equalsIgnoreCase("BEQ")) {
							inputDependency1 = parts[1]; 
							inputDependency2 = parts[2];
							outputDependency = parts[3];
						}
						else if(instructionName.equalsIgnoreCase("ST")) {
							inputDependency1 = parts[1]; 
							outputDependency = parts[2];
							outputDependency2 = parts[3];
						}
						else if ((instructionName.equalsIgnoreCase("ADDI")) || (instructionName.equalsIgnoreCase("SUBI")) || (instructionName.equalsIgnoreCase("ANDI")) || (instructionName.equalsIgnoreCase("ORI")))
						{
							outputDependency = parts[1];
							inputDependency1 = parts[2];
						}
						else{
							outputDependency = parts[1]; 
							inputDependency1 = parts[2];
							inputDependency2 = parts[3];
						}
					
						//Runs the 5 stage MIPS pipeline
						RunPipeline(instructionName, inputDependency1, inputDependency2, outputDependency, outputDependency2, fullInstructionName, instructionArray.size());
						//Add the instruction Frequency in dictionary
						AddInstructionFrequency(instructionName);
				}
				//Fetch the instruction Frequency in dictionary
				GetInstructionFrequency(instructionNameList);
				
				//Execution time of all the instructions
				int executionTime = columnCount-1;
				outputString.add("Execution Time is " + executionTime +" Clock Cycles");
		}
	}

	////Run 5 stage pipelining by running fetch, decode, execute, memory and writeback functions
	private static void RunPipeline(String instructionName, String inputDependency1, String inputDependency2, String outputDependency, String outputDependency2, String fullInstructionName, int instructionCount) {

		//Fill All Instructions data Array  list
		allInstructionDataList = new ArrayList<String>();
		
		//First parameter of the Array to be displayed on grid is the whole instruction String
		allInstructionDataList.add(fullInstructionName);
		
		//Below 5 functions will keep on appending to allInstructionDataList as required
		Fetch();
		Decode();
		Execute(instructionName, inputDependency1, inputDependency2, outputDependency, outputDependency2);
		Memory(instructionName, inputDependency1, inputDependency2, outputDependency, outputDependency2);
		WriteBack(instructionName, inputDependency1, inputDependency2, outputDependency, outputDependency2);
		
		//Creates the final 2 dimensional Array
		listofallLists.add(allInstructionDataList);
		
		//Column count for the grid
		columnCount = allInstructionDataList.size();
		
	}
	
	///// Fetches the instruction and appends to global allInstructionDataList either F 
	///// (standing for fetch) or S (Stall) depending upon the data present in that clockcycle
	private static void Fetch() {
		
		//Gets the register data related to the respective clock cycle stored in the dictionary
		String clockCycleData = (String) dictionary.get(Integer.toString(clockCycle));
		
		//If there is no data and there is a stall in the clockcycle then add stall for this instruction 
		if((clockCycleData != null)&&(clockCycleData.contains("S"))){
			clockCycle++;
			Fetch();
			return;
		}
		
		else{
			//White spaces to be added before the start of instruction in the pipeline
			whiteSpaceCount = clockCycle-1;
			//Add white spaces depending upon the clockcycle from which the fetch instruction starts
			if(whiteSpaceCount!=0){
				for(int j= 0; j<whiteSpaceCount; j++)
				{
					allInstructionDataList.add("");
				}
			}
			allInstructionDataList.add("F");
		}
		fetchClockCounter = clockCycle;
		clockCycle++;
		
	}
	///// Fetches the instruction and appends to global allInstructionDataList either D 
	///// (Decode) or S (Stall) depending upon the data present in that clock cycle
	private static void Decode() {
		
		//Gets the register data related to the respective clock cycle stored in the dictionary
		String clockCycleData = (String) dictionary.get(Integer.toString(clockCycle));
		
		//If there is no data and there is a stall in the clockcycle then add stall for this instruction 
		if((clockCycleData != null)&&(clockCycleData.contains("S"))){
			dictionary.put(Integer.toString(clockCycle).trim(), "S");
			allInstructionDataList.add("S");
			clockCycle++;
			Decode();
			return;
		}
		else{
		allInstructionDataList.add("D");
		}
		clockCycle++;
	}
	
	///// Fetches the instruction and appends to global allInstructionDataList either D 
	///// (Decode) or S (Stall) depending upon the data present in that clock cycle
	private static void Execute(String instructionName, String inputDependency1,
			String inputDependency2, String outputDependency, String outputDependency2) 
	{
		//Gets the register data related to the respective clock cycle stored in the dictionary
		String clockCycleData = (String) dictionary.get(Integer.toString(clockCycle));
		
		if(clockCycleData != null)
		{
			//If there is stall or a RAW Hazard, then add stall in this clock cycle and run the execute instruction again.
			if(clockCycleData.contains("S")||clockCycleData.contains(inputDependency1)||clockCycleData.contains(inputDependency2)){
				
				dictionary.put(Integer.toString(clockCycle).trim(), "S");
				allInstructionDataList.add("S");
				clockCycle++;
				Execute(instructionName,inputDependency1,inputDependency2,outputDependency, outputDependency2);
				return;
			}
			//If its a Store instruction
			else if(instructionName.equals("ST")){
				if((clockCycleData.contains(outputDependency2))){
					
					dictionary.put(Integer.toString(clockCycle).trim(), "S");
					allInstructionDataList.add("S");
					clockCycle++;
					Execute(instructionName,inputDependency1,inputDependency2,outputDependency, outputDependency2);
					return;
				}
				else{
					dictionary.put(Integer.toString(clockCycle).trim(),clockCycleData + "," + inputDependency1 +","+ inputDependency2 + ","+ outputDependency2);
					allInstructionDataList.add("E");
				}
				
			}
			else {
				
				dictionary.put(Integer.toString(clockCycle).trim(),clockCycleData + "," + inputDependency1 +","+ inputDependency2);
				allInstructionDataList.add("E");
			}	
			
		}
		else if(instructionName.equals("ST")) {
			dictionary.put(Integer.toString(clockCycle).trim(), inputDependency1 +","+ outputDependency2);
			allInstructionDataList.add("E");
		}
		else{
			dictionary.put(Integer.toString(clockCycle).trim(), inputDependency1 +","+ inputDependency2);
			allInstructionDataList.add("E");	
		}
		
		clockCycle++;
		
	}
	///// Fetches the instruction and appends to global allInstructionDataList either M 
	///// (Decode) or S (Stall) depending upon the data present in that clock cycle
	private static void Memory(String instructionName, String inputDependency1,
			String inputDependency2, String outputDependency, String outputDependency2) {
		
		//Gets the register data related to the respective clock cycle stored in the dictionary
		String clockCycleData = (String) dictionary.get(Integer.toString(clockCycle));
		
		if(clockCycleData != null){
			//If there is stall or a RAW Hazard, then add stall in this clock cycle and run the execute instruction again.
			if(clockCycleData.contains("S")||clockCycleData.contains(outputDependency)){
				
				dictionary.put(Integer.toString(clockCycle).trim(), "S");
				allInstructionDataList.add("S");
				clockCycle++;
				Memory(instructionName, inputDependency1, inputDependency2, outputDependency, outputDependency2);
				return;
			}
			//If its a store instruction
			else if(instructionName.equals("ST"))
			{
				dictionary.put(Integer.toString(clockCycle).trim(),clockCycleData + "," + outputDependency2);
				allInstructionDataList.add("M");
			}
			else{
				
				dictionary.put(Integer.toString(clockCycle).trim(),clockCycleData + "," + outputDependency);
				allInstructionDataList.add("M");
			}
		}
		else if(instructionName.equals("ST")) {
			dictionary.put(Integer.toString(clockCycle).trim(), outputDependency2);
			allInstructionDataList.add("M");
		}
		else{
			
		dictionary.put(Integer.toString(clockCycle).trim(), outputDependency);
		allInstructionDataList.add("M");
		}
		clockCycle++;
	}
	
	///// Fetches the instruction and appends to global allInstructionDataList either W 
	///// (Decode) or S (Stall) depending upon the data present in that clock cycle
	private static void WriteBack(String instructionName, String inputDependency1,
			String inputDependency2, String outputDependency, String outputDependency2) {
		
		//Gets the register data related to the respective clock cycle stored in the dictionary
		String clockCycleData = (String) dictionary.get(Integer.toString(clockCycle));
		if(clockCycleData != null){
			
			//If there is stall or a RAW Hazard, then add stall in this clock cycle and run the execute instruction again.
			if(clockCycleData.contains("S")||clockCycleData.contains(outputDependency)){
				
				dictionary.put(Integer.toString(clockCycle).trim(), "S");
				allInstructionDataList.add("S");
				clockCycle++;
				WriteBack(instructionName, inputDependency1, inputDependency2, outputDependency, outputDependency2);
				return;
				
			}
			//If its a store instruction
			else if(instructionName.equals("ST")){
				
				dictionary.put(Integer.toString(clockCycle).trim(),clockCycleData + "," + outputDependency2);
				allInstructionDataList.add("W");
			}
			else{
				dictionary.put(Integer.toString(clockCycle).trim(),clockCycleData + "," + outputDependency);
				allInstructionDataList.add("W");
			}
		}
		else if(instructionName.equals("ST")) {
			dictionary.put(Integer.toString(clockCycle).trim(), outputDependency2);
			allInstructionDataList.add("W");
		}
		else{
			dictionary.put(Integer.toString(clockCycle).trim(), outputDependency);
			allInstructionDataList.add("W");
		}
		
		
	}
	////Get the Instruction frequency against each instruction stored in the dictionary
	private static void GetInstructionFrequency(
			ArrayList<String> instructionNameList2) {
		
		//Remove repeated instruction names from the instruction name list using hashset
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(instructionNameList2);
		instructionNameList2.clear();
		instructionNameList2.addAll(hs);
		String instructionName= null;
		//Fetch frequency corresponding to individual instruction
		for(int i=0; i<instructionNameList2.size();i++){
			instructionName = instructionNameList2.get(i);
			String instructionFrequency = dictionary.get(instructionName);
			String output = "Instruction Frequency for "+ instructionName + " instruction is "+ instructionFrequency;
			outputString.add(output);
		}
		
	}
	
	////Add instruction frequency in the dictionary
	private static void AddInstructionFrequency(String instructionName) {
		
		//Get data related to the instruction from dictionary
		String instructionFrequency = dictionary.get(instructionName);
		
		//If instruction is coming for the first time the add frequency 1
		if(instructionFrequency == null){
			dictionary.put(instructionName.trim(), "1");
		}
		else{
			//Increment the frequency and add it to the dictionary
			int instructionFrequencyInt = Integer.parseInt(instructionFrequency);
			instructionFrequency = Integer.toString(instructionFrequencyInt+1);
			dictionary.put(instructionName.trim(), instructionFrequency);
		}
	}
	

}
