package backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Memory {
    private ObservableList<MemoryLine> memory;
    private short address;
    private byte readCounter, writeCounter;

    public Memory() {
        memory = FXCollections.observableArrayList();
        for (short i = 0; i < 2048; i++)
            memory.add(new MemoryLine(i,(short) 0x7000));    // LOCO, acts like NOP inst in CODE segment
        for (short i = 2048; i < 4096; i++)
            memory.add(new MemoryLine(i, (short) 0));

        address = 0;
        readCounter = 0;
        writeCounter = 0;
    }

    public void setMemoryInitial() {
        for (short i = 0; i < 2048; i++)
            memory.get(i).setValue((short) 0x7000);
        for (short i = 2048; i < 4096; i++)
            memory.get(i).setValue((short) 0);

        address = 0;
        readCounter = 0;
        writeCounter = 0;
    }

    public boolean isReadReady() {
        return readCounter == 2;
    }

    public void incrementReadCounter() {
        readCounter++;
    }

    public short read() {
        readCounter = 0;
        return memory.get(address).getValue();
    }

    public boolean isWriteReady() {
        return writeCounter == 2;
    }

    public void incrementWriteCounter() {
        writeCounter++;
    }

    public void write(short value) {
        writeCounter = 0;
        memory.get(address).setValue(value);
    }

    public void setAddress(short address) {
        this.address = address;
    }

    public void write(short address, short value) {
        memory.get(address).setValue(value);
    }

    public void write(short[] codeData) {
        for (short i = 0; i < codeData.length; i++) {
            memory.get(i).setValue(codeData[i]);
        }
    }

    public short read(short address) {
        return memory.get(address).getValue();
    }

    public ObservableList<MemoryLine> getMemory() {
        return memory;
    }
}
