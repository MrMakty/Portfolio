import {Reservation} from './reservation';
import {Spaceship} from './spaceship';

export interface Profile {
  id: number;
  email: String;
  firstName: String;
  lastName: String;
  occupation: String;
  role: {
    name: string;
  };
  reservations: [Reservation];
  spaceships: [Spaceship];
}
