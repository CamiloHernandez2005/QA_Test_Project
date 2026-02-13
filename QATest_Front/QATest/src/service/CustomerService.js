export const CustomerService = {
    getData() {
        return [
            {
                id: 1,
                name: 'SS',
                description: 'Support application',
                lastUpdated: '2025-08-26 10:30:00',
                regions: [
                    { region: 'INT', ip: '10.0.0.1', port: '7000', lastUpdated: '2025-08-26 10:30:00' },
                    { region: 'MX', ip: '10.0.0.2', port: '7001', lastUpdated: '2025-08-26 10:30:00' },
                    { region: 'DOM', ip: '10.0.0.3', port: '7002', lastUpdated: '2025-08-26 10:30:00' }
                ]
            },
            {
                id: 2,
                name: 'Server B',
                description: 'Management application',
                lastUpdated: '2025-08-26 10:30:00',
                regions: [
                    { region: 'INT', ip: '10.0.1.1', port: '7100', lastUpdated: '2025-08-26 10:30:00' },
                    { region: 'MX', ip: '10.0.1.2', port: '7101', lastUpdated: '2025-08-26 10:30:00' },
                    { region: 'DOM', ip: '10.0.1.3', port: '7102', lastUpdated: '2025-08-26 10:30:00' }
                ]
            }
        ];
    },

    getServersSmall() {
        return Promise.resolve(this.getData().slice(0, 5));
    },

    getServersLarge() {
        return Promise.resolve(this.getData());
    }
};
