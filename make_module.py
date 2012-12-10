#!/opt/local/sbin/python

# This Python file uses the following encoding: utf-8
#
# TBD
# - Eliminate command line boiler plate with
#   + argparse (http://docs.python.org/2/library/argparse.html) for Python 2.7+
#   + optparse (http://docs.python.org/2/library/optparse.html) for Python 2.3-7
# - Eliminate logging boilerplate with
#    + logging (http://docs.python.org/2/library/logging.html) for Python 2.3+
# - Use platform to access underlying platform's data (http://docs.python.org/2/library/platform.html)

# @author awong
#
import sys
import os
import getopt

logging = True

class Usage(Exception):
    def __init__(self, msg):
        self.msg = msg

def main(argv=None):
    if argv is None:
        argv = sys.argv
    try:
        try:
            opts, args = getopt.getopt(argv[1:], "hm", ["help","module="])
            execute(opts,args)
        except getopt.error, msg:
             raise Usage(msg)
    except Usage, err:
        print >>sys.stderr, err.msg
        print >>sys.stderr, "for help use --help"
        return 2

def help():
    print "Use me right!"


def execute(opts,args):
    if logging:
        print "opts: "
        print opts
        print "args: " 
        print args

    mname = ""
    if len(args[0]) > 0:
        mname = args[0]
    moduleFactory = ModuleFactory()
    for opt, arg in opts:
        if opt in ("-h", "--help"):
            help()
        elif opt in ("-m", "--module"):
            if len(arg) > 0:
                mname = arg
            elif len(args[0]) > 0:
                mname = args[0]

    if len(mname) > 0:
        print "Creating module named: " + mname
        moduleFactory.create(mname)

class ModuleFactory:
    def create(self, mname):
        self.make_dirs(mname)
        self.make_dirs(mname + "/src")
        self.touch(mname + "/README.md")

        self.make_dirs(mname + "/src/main")
        self.make_dirs(mname + "/src/main/java")
        self.make_dirs(mname + "/src/main/resources")
        self.make_dirs(mname + "/src/main/scala")

        self.touch(mname + "/src/main/java/README.md")
        self.touch(mname + "/src/main/resources/README.md")
        self.touch(mname + "/src/main/scala/README.md")

        self.make_dirs(mname + "/src/test")
        self.make_dirs(mname + "/src/test/java")
        self.make_dirs(mname + "/src/test/resources")
        self.make_dirs(mname + "/src/test/scala")

        self.touch(mname + "/src/test/java/README.md")
        self.touch(mname + "/src/test/resources/README.md")
        self.touch(mname + "/src/test/scala/README.md")
    def make_dirs(self, directory):
        if not os.path.exists(directory):
            os.makedirs(directory)
    def touch(self, fname, times=None):
        text="Boilerplate TBD"
        with file(fname, 'a'):
            os.utime(fname, times)
        with open(fname, 'a') as myfile:
            myfile.write(text)

if __name__ == "__main__":
    sys.exit(main())

