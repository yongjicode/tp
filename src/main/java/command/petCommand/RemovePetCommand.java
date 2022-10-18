package command.petCommand;

import command.Command;
import pet.Pet;
import pet.PetList;

public class RemovePetCommand extends Command {
    public final static String COMMAND_WORD = "remove";
    private int index;

    public RemovePetCommand(int index){
        this.index = index;
    }

    public void execute(){
        int deleteIndex = index - 1;
        PetList.petList.remove(deleteIndex);
        Pet.numOfPets--;
        printPetRemoveMessage();
    }

    public void printPetRemoveMessage(){
        System.out.println("Pet is removed");
    }

    @Override
    public boolean isExit() {
        return false;
    }
}