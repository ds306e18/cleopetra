if __name__ == '__main__':

    try:
        from rlbot import runner
        runner.main()

    except Exception as e:
        print("Encountered exception: ", e)
        print("Press enter to close.")
        input()
