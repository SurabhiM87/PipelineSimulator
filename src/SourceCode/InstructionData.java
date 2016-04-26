//This class has not been used in this program but is added to be used in future
package Logic;

import java.util.List;



public class InstructionData {
	
	private enumInstructions instructionName;
	
	public InstructionData (enumInstructions instruction){
		this.setInstructionName(instruction);
	}
	
	private List<String> inputDependency;
	private String outputDependency;
	
	public List<String> getInputDependency(){
		return inputDependency;
		
	}
	public void setInputDependency(List<String> inputDependency){
		this.inputDependency = inputDependency;
		
	}
	
	public String getOutputDependency(){
		return outputDependency;
	}
	
	public void setOutputDependency(String outputDependency){
		this.outputDependency = outputDependency;
	}
	public enumInstructions getInstructionName() {
		return instructionName;
	}
	public void setInstructionName(enumInstructions instructionName) {
		this.instructionName = instructionName;
	}
	
}


