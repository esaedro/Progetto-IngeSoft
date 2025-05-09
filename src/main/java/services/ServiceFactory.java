package services;

public class ServiceFactory {

    public IUserService createUserService() {
        return new UserServiceImpl(this);
    }

    public IPlaceService createPlaceService() {
        return new PlaceServiceImpl(this);
    }

    public IVisitService createVisitService() {
        return new VisitServiceImpl(this);
    }

    public IBookingService createBookingService() {
        return new BookingServiceImpl();
    }

    public IPersistenceService createPersistenceService() {
        return new PersistenceServiceImpl();
    }
}
