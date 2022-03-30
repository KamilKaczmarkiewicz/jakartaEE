package paintings.painting.view;

import paintings.painting.model.PaintingsModel;
import paintings.painting.service.PaintingService;
import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@RequestScoped
@Named
public class PaintingList implements Serializable {

    private final PaintingService service;

    private PaintingsModel paintings;

    @Inject
    public PaintingList(PaintingService service) {
        this.service = service;
    }

    public PaintingsModel getPaintings() {
        if (paintings == null) {
            paintings = PaintingsModel.entityToModelMapper().apply(service.findAll());
        }
        return paintings;
    }

    public String deleteAction(PaintingsModel.Painting painting) {
        service.delete(painting.getId());
        return "painting_list?faces-redirect=true";
    }

}
