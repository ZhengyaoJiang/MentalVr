using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class Manager : MonoBehaviour {
	
	Move move;
    Text text;
    Animator text_animator;
	int i=0;

	void Start () {
		this.name = "Manager";
		move = GameObject.Find ("Manager").GetComponent<Move>();
		text = GameObject.Find ("Text").GetComponent<Text>();
        text_animator = GameObject.Find("Text").GetComponent<Animator>();
    }

	void Update () {
	}

	private void trainStart(Command command)
    {
		switch (command) {
		case Command.Push:
            
			move.moveContinuously (Move.MoveDirection.FORWARD);
			break;
        case Command.Natural:
            
            move.moveContinuously(Move.MoveDirection.STOP);
            break;
        }
	}

	public void commandMove(string command){
		switch(command){
		case "PUSH":
			move.moveForward();
			break;
		case "NEUTRAL":
			move.stopMove ();
			break;
		}
	}

	public void trainStop(){
		move.stopMoveContinuously ();
	}

	public void setStage(string stage){
        int int_stage = int.Parse(stage);

        switch (int_stage)
        {
            case 1:              
                text.text = "natural state, don't imagine motion";
                trainStart(Command.Natural);
                break;
            case 2:
                trainStop();
                break;
            case 3:
                text.text = "imagine to move forward";
                trainStart(Command.Push);
                break;
            case 4:
                text.text = "";
                trainStop();
                break;
            case 5:
                break;
        }
        text_animator.SetInteger("stage", int_stage);

    }
}

enum Command
{
    Push,
    Natural
}
