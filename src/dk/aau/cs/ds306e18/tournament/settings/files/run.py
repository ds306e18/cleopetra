import time
import socket
from threading import Event, Thread

from rlbot.setup_manager import SetupManager, setup_manager_context


PORT = 35353


class Command:
    START = b"START"
    STOP = b"STOP"
    EXIT = b"EXIT"


def setup_match(manager: SetupManager):
    manager.shut_down(kill_all_pids=True, quiet=True)  # Stop any running pids
    manager.load_config()
    manager.launch_early_start_bot_processes()
    manager.start_match()
    manager.launch_bot_processes()


def wait_for_all_bots(manager: SetupManager):
    while not manager.has_received_metadata_from_all_bots():
        manager.try_recieve_agent_metadata()
        time.sleep(0.1)


def start_match(manager: SetupManager):
    game_interface = manager.game_interface
    setup_match(manager)
    wait_for_all_bots(manager)


def stop_match(manager: SetupManager):
    manager.shut_down(kill_all_pids=True, quiet=True)


def match_running(start_event: Event, stop_event: Event, exit_event: Event):
    with setup_manager_context() as manager:
        while True:
            if start_event.is_set():
                start_match(manager)
                start_event.clear()
            elif stop_event.is_set():
                stop_match(manager)
                stop_event.clear()
            elif exit_event.is_set():
                exit_event.clear()
                break


if __name__ == '__main__':
    print("""/<<<<<<<<<<<<<<<<<<<o>>>>>>>>>>>>>>>>>>>\\
| Hello! I am CleoPetra's little helper |
| and your view into the RLBot process. |
| Keep me open while using CleoPetra!   |
\\<<<<<<<<<<<<<<<<<<<o>>>>>>>>>>>>>>>>>>>/
""")

    try:
        start_event = Event()
        stop_event = Event()
        exit_event = Event()

        match_runner = Thread(target=match_running, args=(start_event, stop_event, exit_event))
        match_runner.start()

        # AF_INET is the Internet address family for IPv4. SOCK_STREAM is the socket type for TCP
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.bind(('127.0.0.1', PORT))
            s.listen()
            print(f"Listening for CleoPetra on 127.0.0.1:{PORT} ...")
            while True:
                conn, addr = s.accept()
                with conn:
                    print('Connected by', addr)
                    data = conn.recv(1024)

                    if data == Command.START:
                        print("Starting match!")
                        start_event.set()
                    elif data == Command.STOP:
                        print("Stopping match!")
                        stop_event.set()
                    elif data == Command.EXIT:
                        print("Exiting!")
                        exit_event.set()
                        break
                    else:
                        print(f"Unknown command received: {data}")

                    conn.sendall(b'Gotcha!')

        match_runner.join()

    except Exception as e:
        print("Encountered exception: ", e)
        print("Press enter to close.")
        input()
    finally:
        print("run.py stopped")
