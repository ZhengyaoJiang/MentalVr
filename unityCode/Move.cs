using UnityEngine;
using System.Collections;

public class Move : MonoBehaviour {
	public enum MoveDirection{
		FORWARD,
		BACKWARD,
		STOP
	}
    Vector3 origin;
	bool is_trainning=false;
	MoveDirection move_direction=MoveDirection.STOP;
	// Use this for initialization
	void Start () {
        origin = transform.position;
	}
	
	// Update is called once per frame
	void Update () {
		switch (move_direction) {
		case MoveDirection.FORWARD:
            Quaternion q= transform.FindChild("Head").transform.rotation;
			transform.position += q*new Vector3 (0f, 0f, 0.1f);
			break;
		case MoveDirection.STOP:			
			break;
		}
	}

	public void moveContinuously(MoveDirection direction){
		move_direction = direction;
		is_trainning = true;
	}


	public void stopMoveContinuously(){
		move_direction = MoveDirection.STOP;
        transform.position = origin;
        is_trainning = false;
	}

	public void moveForward(){
		if(!is_trainning)
			move_direction = MoveDirection.FORWARD;
	}

	public void stopMove(){
		if(!is_trainning)
			move_direction = MoveDirection.STOP;
	}
}
