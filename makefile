SRC_DIR = ./src
BIN_DIR = ./bin
LAB_DIR = ./lab

JFLAGS = -g
CPFLAGS = -cp $(BIN_DIR)
SRCFLAGS = -sourcepath $(SRC_DIR)
BINFLAGS = -d $(BIN_DIR)

TERMINALFILE = MazeEvasion
GUIFILE = MazeEvasionGUI
GENFILE = MazeGenerator
DEFAULT_LAB = labyrinthe.txt
LAB = $(LAB_DIR)/$(DEFAULT_LAB)
DIM = 8
NCANDY = 9
NMONSTER = 9


JC = javac
JVM = java

.SUFFIXES : .class .java


FILES = \
	$(SRC_DIR)/be/evasion/main/$(TERMINALFILE).java \
	$(SRC_DIR)/be/evasion/gui/$(GUIFILE).java \
	$(SRC_DIR)/be/evasion/util/$(GENFILE).java \

CLS= $(FILES:.java=.class)
RUN_ARGS=$(DEFAULT_LAB)
ifeq (run,$(firstword $(MAKECMDGOALS)))
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  $(eval $(RUN_ARGS):;@:)
endif
ifeq (gui,$(firstword $(MAKECMDGOALS)))
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  $(eval $(RUN_ARGS):;@:)
endif


default:
	@echo ""
	@echo "make build - build the projects"
	@echo "make run 'filename' - run the command line interface for the filename given"
	@echo "make gui 'filename' - run the graphic user interface for the filename given"
	@echo "make clean	- remove *.class files"
	@echo "make clean-bin	- remove the $(BIN_DIR) directory"
	@echo ""
	@echo "!!! The filename must be in the $(LAB_DIR) directory"
	@echo "!!! Don't forget the filename for make run/gui"
	@echo "Example of execution:"
	@echo "make run labyrinthe.txt"
	@echo "make gui labyrinthe.txt"
	@echo ""


build: clean create $(CLS)
.java.class :
	$(JC) $(JFLAGS) $(BINFLAGS) $(CPFLAGS) $(SRCFLAGS)  $<

create:
ifeq ("$(wildcard $(BIN_DIR))","")
	mkdir $(BIN_DIR)
endif

clean:
	find . -type f -name '*.class' -delete
clean-bin:
	rm -r $(BIN_DIR)

run:
	@$(JVM) -cp $(BIN_DIR) be.evasion.main.$(TERMINALFILE) $(LAB_DIR)/$(RUN_ARGS)
gui:
	@$(JVM) -cp $(BIN_DIR) be.evasion.gui.$(GUIFILE) $(LAB_DIR)/$(RUN_ARGS)
gen:
	@$(JVM) -cp $(BIN_DIR) be.evasion.util.$(GENFILE) $(DIM) $(DIM) $(NMONSTER) $(NCANDY) > $(LAB)
genrun: gen run
gengui: gen gui

