package paintings.painting.view;

import lombok.Getter;
import lombok.Setter;
import paintings.painting.entity.Painting;
import paintings.painting.model.PaintingModel;
import paintings.painting.service.PaintingService;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@RequestScoped
@Named
public class PaintingView implements Serializable {

    private final PaintingService service;

    @Setter
    @Getter
    private Long id;

    @Getter
    private PaintingModel painting;

    @Inject
    public PaintingView(PaintingService service) {
        this.service = service;
    }

    public void init() throws IOException {
        Optional<Painting> painting = service.find(id);
        if (painting.isPresent()) {
            this.painting = PaintingModel.entityToModelMapper().apply(painting.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Painting not found");
        }
    }

    
}
