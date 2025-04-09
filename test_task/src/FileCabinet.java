import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

interface Cabinet {
    // zwraca dowolny element o podanej nazwie
    Optional<Folder>
    findFolderByName(String name);

    // zwraca wszystkie foldery podanego rozmiaru SMALL/MEDIUM/LARGE
    List<Folder> findFoldersBySize(String size);

    //zwraca liczbę wszystkich obiektów tworzących strukturę
    int count();
}

public class FileCabinet implements Cabinet {
    private List<Folder> folders;

    @Override
    public Optional<Folder> findFolderByName(String name) {
        for (Folder folder : folders) {
            Optional<Folder> result = findByNameRecursive(folder, name);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    private Optional<Folder> findByNameRecursive(Folder folder, String name) {
        if (folder.getName().equals(name)) {
            return Optional.of(folder);
        }

        if (folder instanceof MultiFolder multi) {
            for (Folder child : multi.getFolders()) {
                Optional<Folder> result = findByNameRecursive(child, name);
                if (result.isPresent()) {
                    return result;
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Folder> findFoldersBySize(String size) {
        List<Folder> result = new ArrayList<>();
        for (Folder folder : folders) {
            findBySizeRecursive(folder, size, result);
        }
        return result;
    }

    private void findBySizeRecursive(Folder folder, String size, List<Folder> result) {
        if (folder.getSize().equals(size)) {
            result.add(folder);
        }

        if (folder instanceof MultiFolder multi) {
            for (Folder child : multi.getFolders()) {
                findBySizeRecursive(child, size, result);
            }
        }
    }
    @Override
    public int count() {
        int total = 0;
        for (Folder folder : folders) {
            total += countFolders(folder);
        }
        return total;
    }

    private int countFolders(Folder folder) {
        int count = 1;

        if (folder instanceof MultiFolder multi) {
            for (Folder f : multi.getFolders()) {
                count += countFolders(f);
            }
        }

        return count;
    }
}

interface Folder {
    String getName();
    String getSize();
}

interface MultiFolder extends Folder {
    List<Folder> getFolders();
}