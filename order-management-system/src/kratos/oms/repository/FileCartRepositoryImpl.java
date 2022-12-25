package kratos.oms.repository;

import kratos.oms.domain.Cart;
import kratos.oms.seedwork.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileCartRepositoryImpl extends BaseFileRepository<UUID, Cart> implements CartRepository {
    private final static String DATA_FILE_NAME = "carts.txt.";

    public FileCartRepositoryImpl(String directoryUrl) {
        super(directoryUrl, DATA_FILE_NAME);
    }

    @Override
    public Optional<Cart> findByAccountId(UUID accountId) {
        return listAll().stream().filter(a -> a.getAccountId().equals(accountId)).findFirst();
    }

    @Override
    public boolean addOrUpdate(Cart cart) {
        List<Cart> carts = listAll();
        Optional<Cart> cartOpt = findByAccountId(cart.getAccountId());
        if(cartOpt.isPresent()) {
            carts.removeIf(c -> c.getAccountId().equals(cart.getAccountId()));
        }
        carts.add(cart);
        try {
            this.write(carts);
            return true;
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "addOrUpdate", e);
            return false;
        }
    }

    private List<Cart> listAll() {
        try {
            return new ArrayList<>(this.read(Cart.class));
        } catch (IOException e) {
            Logger.printError(this.getClass().getName(), "listAll", e);
            return new ArrayList<>();
        }
    }
}
