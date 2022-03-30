package paintings.painting.view;

import lombok.Getter;
import lombok.Setter;
import paintings.painting.entity.Painting;
import paintings.painting.model.PaintingEditModel;
import paintings.painting.service.PaintingService;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;

@ViewScoped
@Named
public class PaintingEdit implements Serializable {

    private PaintingService paintingService;

    @Setter
    @Getter
    private Long id;

    @Getter
    private PaintingEditModel painting;

    @EJB
    public void setPaintingService(PaintingService paintingService){
        this.paintingService = paintingService;
    }

    public void init() throws IOException {
        Optional<Painting> painting = paintingService.find(id);
        if (painting.isPresent()) {
            this.painting = PaintingEditModel.entityToModelMapper().apply(painting.get());
        } else {
            FacesContext.getCurrentInstance().getExternalContext()
                    .responseSendError(HttpServletResponse.SC_NOT_FOUND, "Painting not found");
        }
    }

    public String saveAction() throws IOException {
        try {
            paintingService.update(PaintingEditModel
                    .modelToEntityUpdater()
                    .apply(paintingService.find(id).orElseThrow(), painting));
            return "painting_list";
        } catch (EJBException ex) {
            if (ex.getCause() instanceof OptimisticLockException) {
                init();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Version collision"));
            }
            return null;
        }
    }


}
