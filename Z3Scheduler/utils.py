

# This function reveals the nus_classes of the module
def enumerate_nus_classes(nus_class) :
    print(str(len(nus_class.lectures)) + " lectures")
    print(str(len(nus_class.tutorials)) + " tutorials")
    print(str(len(nus_class.recitations)) + " recitations")
    print(str(len(nus_class.seminars)) + " seminars")
    print(str(len(nus_class.labs)) + " labs\n")
