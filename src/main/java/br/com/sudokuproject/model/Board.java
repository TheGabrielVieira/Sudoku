package br.com.sudokuproject.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static br.com.sudokuproject.model.GameStatusEnum.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus(){
        if (spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))){
            return NON_STARTED;
        }

        return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETE;
    }

    public boolean hasErrors(){
        for (int index = 0; index < 9; index++) {
            if (hasDuplicates(getSpacesInRow(index)) || hasDuplicates(getSpacesInColumn(index))) {
                return true;
            }
        }

        for (int startCol = 0; startCol < 9; startCol += 3) {
            for (int startRow = 0; startRow < 9; startRow += 3) {
                if (hasDuplicates(getSpacesInSector(startCol, startRow))) {
                    return true;
                }
            }
        }

        return false;
    }

    private List<Space> getSpacesInRow(final int row) {
        return spaces.stream().map(column -> column.get(row)).toList();
    }

    private List<Space> getSpacesInColumn(final int column) {
        return spaces.get(column);
    }

    private List<Space> getSpacesInSector(final int startCol, final int startRow) {
        return java.util.stream.IntStream.range(startCol, startCol + 3)
                .boxed()
                .flatMap(col -> java.util.stream.IntStream.range(startRow, startRow + 3)
                        .mapToObj(row -> spaces.get(col).get(row)))
                .toList();
    }

    private boolean hasDuplicates(final List<Space> group) {
        final Set<Integer> values = new HashSet<>();
        return group.stream()
                .map(Space::getActual)
                .filter(value -> nonNull(value))
                .anyMatch(value -> !values.add(value));
    }

    public boolean changeValue(final int col, final int row, final int value){
        var space = spaces.get(col).get(row);
        if (space.isFixed()){
            return false;
        }

        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row){
        var space = spaces.get(col).get(row);
        if (space.isFixed()){
            return false;
        }

        space.clearSpace();
        return true;
    }

    public void reset(){
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished(){
        return !hasErrors() && getStatus().equals(COMPLETE);
    }
}
