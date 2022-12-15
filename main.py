class State:
    def __init__(self, name):
        self.name = name


class Event:
    def __init__(self, name):
        self.name = name


class CurrentStateEvent:
    def __init__(self, state: State, event: Event):
        self.event = event
        self.state = state


class TransitionTable:
    stateTable: dict[str, State] = dict()

    def addTranstition(self, current: State, event: Event, next: State):
        currentStateEvent = CurrentStateEvent(current, event)
        self.stateTable[f"{currentStateEvent.state.name}{currentStateEvent.event.name}"] = next

    def getNextState(self, currentState: CurrentStateEvent):
        if f"{currentState.state.name}{currentState.event.name}" in self.stateTable.keys():
            return self.stateTable[f"{currentState.state.name}{currentState.event.name}"]
        else:
            return None

    def getStates(self):
        result = set()
        result.add(self.stateTable.keys())
        result.add(self.stateTable.values())
        return list(result)


class FiniteStateMachine:
    def __init__(self, initial: State, transitionTable: TransitionTable, accepting: list[State]):
        self.initial = initial
        self.transitionTable = transitionTable
        self.accepting = accepting
        self.state = initial

    def send(self, event: Event):
        currentStateEvent = CurrentStateEvent(self.state, event)
        nextState = self.transitionTable.getNextState(currentStateEvent)
        print(nextState.name)
        if nextState is not None:
            print(f"received {event.name}. Transitioning from {self.state.name} to {nextState.name}")
            self.state = nextState
            for i in accepting:
                if i.name == self.state.name:
                    return True
        return False

    def sendList(self, events: list):
        done = False
        for event in events:
            done = self.send(event)
        return done


states = list()
accepting = list()
events = list()
number_states = int(input("enter number of states"))
number_inputs = int(input("enter number of inputs"))
for i in range(0, number_states):
    state_name = str(input(f"enter state {i + 1} name"))
    state = State(state_name)
    states.append(state)

for i in range(0, number_inputs):
    input_name = str(input(f"enter input {i + 1} value"))
    event = Event(input_name)
    events.append(event)

table = TransitionTable()
for i in range(0, number_states):
    currentState = states[i]
    choise = int(input(f"{currentState.name} is an accepting \n 1.yes \n 2.no"))

    if i == 0:
        start_state = currentState

    if choise == 1:
        accepting.append(currentState)
    elif choise == 2:
        print("ok")
    else:
        print("input error")

    for j in range(0, number_inputs):
        print(f"{currentState.name} gets {events[j].name} where to go ?")
        event = events[j]
        for k in range(0, len(states)):
            print(f"{k + 1}.{states[k].name}")

        choise = int(input())
        if choise in range(0, len(states) + 1):
            next_state = states[choise - 1]
        table.addTranstition(currentState, event, next_state)
fsm = FiniteStateMachine(start_state, table, accepting)

print("table")
for i in range(0, len(states)):
    for j in range(0, len(events)):
        print(f"state {states[i].name} goes to "
              f"{fsm.transitionTable.getNextState(CurrentStateEvent(states[i], events[j])).name} "
              f"when it gets input {events[j].name}")

for i in range(0, len(accepting)):
    print(f"state {accepting[i].name} is an accepting state ")

for i in range(0, 10):
    print(f"input ")
    input_get = str(input("enter string"))
    test_event = list()
    for i in input_get:
        ev = Event(i)
        test_event.append(ev)
    accepted = fsm.sendList(test_event)
    if accepted:
        print("string accepted")
    else:
        print("not accepted")
