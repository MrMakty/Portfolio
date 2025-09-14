

export interface Reservation {
  id: number; // optioneel bij aanmaken
  dock: any;   // of specifieker: { id: number; name?: string; } als je dat weet
  spaceship: any;
  startTimestamp: Date;
  endTimestamp: Date;
}
