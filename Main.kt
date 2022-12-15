data class State(val state: String)

data class Event(val event: String)

data class CurrentStateEvent(val state: State, val event: Event)

class TransitionTable {
    private var stateTable = mutableMapOf<CurrentStateEvent, State>()

    fun addTransition(current: State, event: Event, next: State) {
        val currentStateEvent = CurrentStateEvent(current, event)
        stateTable[currentStateEvent] = next
    }

    fun getNextState(currentStateEvent: CurrentStateEvent): State? {
        return when (currentStateEvent) {
            in stateTable ->  {
                stateTable[currentStateEvent]
            }
            else -> null
        }
    }
    fun getStates(): List<State> {
        var result = mutableSetOf<State>()
        stateTable.filterKeys { result.add(it.state) }
        stateTable.filterValues { result.add(it) }
        return result.toList()
    }
    fun getEvents(): List<Event> {
        var result = mutableSetOf<Event>()
        stateTable.filterKeys { result.add(it.event) }
        return result.toList()
    }
}

class FiniteStateMachine constructor(initial: State, val transitionTable: TransitionTable, val accepting: List<State>) {
    var state = initial
    private val error = State("ERROR")

    fun send(event: Event): Boolean {
        val currentStateEvent = CurrentStateEvent(state, event)
        val nextState = transitionTable.getNextState(currentStateEvent) ?: error
        println("Received $event. Transitioning from $state to $nextState")
        state = nextState
        if (state in accepting) {
            return true
        }
        return false
    }

    fun send(events: List<Event>): Boolean {
        var done=false
        for (event in events) {
            done = send(event)
        }
        return done
    }
}

fun main(args: Array<String>) {

    var states:MutableList<State> = mutableListOf()
    var accepting:MutableList<State> = mutableListOf()
    var events:MutableList<Event> = mutableListOf()
    var number_states:Int
    var number_input:Int
    println("enter number of states")
    number_states= readln().toInt()
    println("enter number of inputs")
    number_input= readln().toInt()
    for (i in 0 until number_states){

        println("enter state ${i+1} name")
        val state=State(readln())
        states.add(state)
    }
    for (i in 0 until number_input){

        println("enter input ${i+1}")
        val event=Event(readln())
        events.add(event)
    }
    val table = TransitionTable()
    lateinit var start_state:State
    var current_state:State
    for (i in 0 until number_states){
        current_state=states[i]
        println("${current_state} is an accepting state")
        println("1.yes")
        println("2.no")
        when(i){
            0->{
                start_state=current_state
            }
        }
        when(readln().toInt()){
            1-> accepting.add(current_state)
            2-> println("done")
            else-> println("input error")
        }
        for (j in 0 until number_input){
            println("${current_state.state} gets ${events[j]} where to go ?")
            val event=events[j]
            var range:Int=0
            for (k in 0 until states.size){
                println("${k+1}.${states[k]}")
                range=k
            }
            lateinit var next_state:State
            val choise= readln().toInt()
            when(choise){
                in 0..range+1 -> next_state=states[choise-1]
                else -> println("error")

            }
            table.addTransition(current_state,event,next_state)
        }
    }
    val fsm = FiniteStateMachine(start_state, table, accepting)
    println("input 1")
    val accepted = fsm.send(listOf<Event>(Event("a"),Event("b"),Event("c")))


    if (accepted) {
        println("In accepting state ${fsm.state}")
    } else {
        println("Not in accepting state ${fsm.state}")
    }
    println("input 2")
    val accepted1 = fsm.send(listOf<Event>(Event("b"),Event("c"),Event("a"),Event("c"),Event("b"),Event("a")))


    if (accepted1) {
        println("In accepting state ${fsm.state}")
    } else {
        println("Not in accepting state ${fsm.state}")
    }
    println("input 3")
    val accepted2 = fsm.send(listOf<Event>(Event("c"),Event("c"),Event("c"),Event("a")))


    if (accepted2) {
        println("In accepting state ${fsm.state}")
    } else {
        println("Not in accepting state ${fsm.state}")
    }
    println("input 4")
    val accepted3 = fsm.send(listOf<Event>(Event("a"),Event("b"),Event("b")))


    if (accepted3) {
        println("In accepting state ${fsm.state}")
    } else {
        println("Not in accepting state ${fsm.state}")
    }


    println("table")
    for (i in 0 until states.size){
        for ( j in 0 until events.size){
            println("state ${states[i]} goes to" +
                    " ${fsm.transitionTable.getNextState(CurrentStateEvent(states[i],events[j]))} " +
                    "when it gets input ${events[j]}")
        }
    }
   for(i in 0 until accepting.size) {
     println("state ${accepting[i]} is an accepting state")
  }
    println(fsm.transitionTable.getStates())
    println(fsm.transitionTable.getEvents())
}